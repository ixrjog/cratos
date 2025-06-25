package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.ApplicationResource;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.param.http.application.ApplicationParam;
import com.baiyi.cratos.domain.view.application.ApplicationVO;
import com.baiyi.cratos.facade.application.ApplicationResourceFacade;
import com.baiyi.cratos.service.ApplicationResourceService;
import com.baiyi.cratos.service.ApplicationService;
import com.baiyi.cratos.service.EdsAssetService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/15 15:43
 * &#064;Version 1.0
 */
public class ApplicationFacadeTest extends BaseUnit {

    @Resource
    private ApplicationFacade applicationFacade;

    @Resource
    private ApplicationService applicationService;

    @Resource
    private ApplicationResourceFacade applicationResourceFacade;

    @Resource
    private ApplicationResourceService resourceService;
    @Resource
    private EdsAssetService edsAssetService;

    @Test
    void voTest() {
        ApplicationParam.ApplicationPageQuery pageQuery = ApplicationParam.ApplicationPageQuery.builder()
                .page(1)
                .length(1)
                .build();
        DataTable<ApplicationVO.Application> table = applicationFacade.queryApplicationPage(pageQuery);
        System.out.println(table);
    }

    @Test
    void resourceScanTest1() {
        applicationResourceFacade.scan("kili");
    }

    @Test
    void resourceScanTest2() {
        applicationResourceFacade.scanAll();
    }

    /**
     * 数据订正
     */
    @Test
    void test3() {
        List<ApplicationResource> resourceList = resourceService.selectAll();
        resourceList.forEach(e -> {
            if (BusinessTypeEnum.EDS_ASSET.name()
                    .equals(e.getBusinessType())) {
                EdsAsset asset = edsAssetService.getById(e.getBusinessId());
                if (Objects.isNull(asset)) {
                    System.out.println(
                            "ID=" + e.getId() + ",Application Name=" + e.getApplicationName() + ",Instance name: " + e.getInstanceName() + ", name=" + e.getName());
                    applicationResourceFacade.deleteById(e.getId());
                }

            }
        });

    }

}