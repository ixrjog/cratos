package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.application.ApplicationParam;
import com.baiyi.cratos.domain.view.application.ApplicationVO;
import com.baiyi.cratos.facade.application.ApplicationFacade;
import com.baiyi.cratos.facade.application.ApplicationResourceFacade;
import com.baiyi.cratos.service.ApplicationService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

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

}