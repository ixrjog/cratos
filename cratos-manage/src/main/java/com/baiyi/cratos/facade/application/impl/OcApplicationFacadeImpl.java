package com.baiyi.cratos.facade.application.impl;

import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.param.http.application.ApplicationParam;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.config.loader.EdsOpscloudConfigLoader;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.opscloud.model.OcApplicationVO;
import com.baiyi.cratos.eds.opscloud.repo.OcApplicationRepo;
import com.baiyi.cratos.facade.ApplicationFacade;
import com.baiyi.cratos.facade.EdsFacade;
import com.baiyi.cratos.facade.application.OcApplicationFacade;
import com.baiyi.cratos.facade.application.model.ApplicationConfigModel;
import com.baiyi.cratos.service.ApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/29 10:03
 * &#064;Version 1.0
 */
@Component
@AllArgsConstructor
public class OcApplicationFacadeImpl implements OcApplicationFacade {

    private final ApplicationService applicationService;
    private final ApplicationFacade applicationFacade;
    private final BusinessTagFacade businessTagFacade;
    private final EdsFacade edsFacade;
    private final EdsOpscloudConfigLoader edsOpscloudConfigLoader;

    private static final String[] LEVEL_TAGS = {"A1", "A2", "A3", "B1", "B2", "B3", "C1", "C2", "C3"};

    private void importAllApplication(List<OcApplicationVO.Application> ocApplications) {
        if (CollectionUtils.isEmpty(ocApplications)) {
            return;
        }
        ocApplications.forEach(ocApp -> {
            Application application = getOrCreateApplication(ocApp);
            // 同步标签
            importApplicationTags(ocApp, application);
            // 同步仓库
            importApplicationGitLab(ocApp, application);
        });
    }

    private Application getOrCreateApplication(OcApplicationVO.Application ocApplication) {
        Application application = applicationService.getByName(ocApplication.getName());
        if (Objects.nonNull(application)) {
            return application;
        }
        ApplicationParam.AddApplication addApplication = ApplicationParam.AddApplication.builder()
                .name(ocApplication.getName())
                .comment(ocApplication.getComment())
                .valid(ocApplication.getIsActive())
                .build();
        applicationFacade.addApplication(addApplication);
        return applicationService.getByName(ocApplication.getName());
    }

    private void importApplicationGitLab(OcApplicationVO.Application ocApplication, Application application) {
        ApplicationConfigModel.Config config = ApplicationConfigModel.loadAs(application);
        if (!CollectionUtils.isEmpty(ocApplication.getResourceMap()) && ocApplication.getResourceMap()
                .containsKey("GITLAB_PROJECT")) {
            String gitSsh = ocApplication.getResourceMap()
                    .get("GITLAB_PROJECT")
                    .getFirst()
                    .getName();
            ApplicationConfigModel.Repository repository = ApplicationConfigModel.Repository.builder()
                    .sshUrl(gitSsh)
                    .type("gitLab")
                    .build();
            config.setRepository(repository);
            application.setConfig(config.dump());
            applicationService.updateByPrimaryKey(application);
        }
    }

    // 同步标签
    private void importApplicationTags(OcApplicationVO.Application ocApplication, Application application) {
        if (CollectionUtils.isEmpty(ocApplication.getTags())) {
            return;
        }
        ocApplication.getTags()
                .forEach(tag -> Arrays.stream(LEVEL_TAGS)
                        .filter(levelTag -> levelTag.equals(tag.getTagKey()))
                        .map(levelTag -> BusinessTagParam.SaveBusinessTag.builder()
                                .businessId(application.getId())
                                .businessType(BusinessTypeEnum.APPLICATION.name())
                                // Level
                                .tagId(37)
                                .tagValue(tag.getTagKey())
                                .build())
                        .forEach(businessTagFacade::saveBusinessTag));
    }

    @Override
    public void importAllApplication() {
        edsFacade.queryValidEdsInstanceByType(EdsInstanceTypeEnum.OPSCLOUD.name())
                .forEach(instance -> {
                    EdsConfigs.Opscloud opscloud = edsOpscloudConfigLoader.getConfig(
                            instance.getConfigId());
                    List<OcApplicationVO.Application> ocApps = OcApplicationRepo.listApplication(opscloud);
                    importAllApplication(ocApps);
                });
    }

}
