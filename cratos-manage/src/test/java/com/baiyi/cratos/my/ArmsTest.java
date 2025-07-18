package com.baiyi.cratos.my;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.service.ApplicationService;
import com.baiyi.cratos.service.EdsAssetService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/18 09:51
 * &#064;Version 1.0
 */
public class ArmsTest extends BaseUnit {

    @Resource
    private EdsAssetService edsAssetService;

    @Resource
    private ApplicationService applicationService;

    @Test
    void test() {
        List<EdsAsset> armsApps = edsAssetService.queryInstanceAssets(93,
                EdsAssetTypeEnum.ALIYUN_ARMS_TRACE_APPS.name());
        List<Application> applications = applicationService.selectAll();
        Set<String> appSet = applications.stream()
                .map(Application::getName)
                .collect(Collectors.toSet());

        armsApps.forEach(armsApp -> {
//            String env = armsApp.getName()
//                    .substring(armsApp.getName()
//                            .lastIndexOf('-') + 1);
            String appName = armsApp.getName()
                    .substring(0, armsApp.getName()
                            .lastIndexOf('-'));
            if (!appSet.contains(appName)) {
                System.out.println(armsApp.getName());
            }
        });
    }

}
