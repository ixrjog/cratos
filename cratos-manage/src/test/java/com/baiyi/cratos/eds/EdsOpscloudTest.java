package com.baiyi.cratos.eds;

import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.eds.core.config.EdsOpscloudConfigModel;
import com.baiyi.cratos.eds.opscloud.repo.OcApplicationRepo;
import com.baiyi.cratos.eds.opscloud.vo.OcApplicationVO;
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
public class EdsOpscloudTest extends BaseEdsTest<EdsOpscloudConfigModel.Opscloud> {

    @Resource
    private ApplicationService applicationService;

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
            if (applicationService.getByName(ocApp.getName()) == null) {
                applicationService.add(application);
                System.out.println(application.getName());
            }
        });
    }

}
