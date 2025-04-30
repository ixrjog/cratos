package com.baiyi.cratos.eds.opscloud;

import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.eds.BaseEdsTest;
import com.baiyi.cratos.eds.core.config.EdsOpscloudConfigModel;
import com.baiyi.cratos.eds.opscloud.model.OcApplicationVO;
import com.baiyi.cratos.eds.opscloud.repo.OcApplicationRepo;
import com.baiyi.cratos.facade.application.OcApplicationFacade;
import com.baiyi.cratos.facade.application.model.ApplicationConfigModel;
import com.baiyi.cratos.service.ApplicationService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/2 13:46
 * &#064;Version 1.0
 */
public class EdsOpscloudApplicationTest extends BaseEdsTest<EdsOpscloudConfigModel.Opscloud> {

    @Resource
    private ApplicationService applicationService;
    @Resource
    private BusinessTagFacade businessTagFacade;

    @Resource
    private OcApplicationFacade ocApplicationFacade;

    private static final String[] LEVEL_TAGS = {"A1", "A2", "A3", "B1", "B2", "B3", "C1", "C2", "C3"};

    @Test
    void importOcApplicationTest() {
        EdsOpscloudConfigModel.Opscloud cfg = getConfig(32);
        List<OcApplicationVO.Application> ocApps = OcApplicationRepo.listApplication(cfg);
        if (CollectionUtils.isEmpty(ocApps)) {
            return;
        }
        ocApps.forEach(ocApp -> {
            Application application = Application.builder()
                    .name(ocApp.getName())
                    .comment(ocApp.getComment())
                    .valid(ocApp.getIsActive())
                    .build();
            Application applicationFromDB = applicationService.getByName(ocApp.getName());
            if (applicationFromDB == null) {
                applicationService.add(application);
            } else {
                application = applicationFromDB;
            }
            System.out.println(application.getName());
            // 同步标签
            if (!CollectionUtils.isEmpty(ocApp.getTags())) {
                for (OcApplicationVO.Tag tag : ocApp.getTags()) {
                    for (String levelTag : LEVEL_TAGS) {
                        if (levelTag.equals(tag.getTagKey())) {
                            BusinessTagParam.SaveBusinessTag saveBusinessTag = BusinessTagParam.SaveBusinessTag.builder()
                                    .businessId(application.getId())
                                    .businessType(BusinessTypeEnum.APPLICATION.name())
                                    // Level
                                    .tagId(37)
                                    .tagValue(tag.getTagKey())
                                    .build();
                            businessTagFacade.saveBusinessTag(saveBusinessTag);
                        }
                    }
                }
            }
            // 同步仓库

            ApplicationConfigModel.Config config = ApplicationConfigModel.loadAs(application);

            if (!CollectionUtils.isEmpty(ocApp.getResourceMap()) && ocApp.getResourceMap()
                    .containsKey("GITLAB_PROJECT")) {
                String gitSsh = ocApp.getResourceMap()
                        .get("GITLAB_PROJECT")
                        .getFirst()
                        .getName();
                System.out.println(gitSsh);
                ApplicationConfigModel.Repository repository = ApplicationConfigModel.Repository.builder()
                        .sshUrl(gitSsh)
                        .type("gitLab")
                        .build();

                config.setRepository(repository);
                application.setConfig(config.dump());
                applicationService.updateByPrimaryKey(application);
            }
        });
    }

    @Test
    void test2() {
        ocApplicationFacade.importAllApplication();
    }

}
