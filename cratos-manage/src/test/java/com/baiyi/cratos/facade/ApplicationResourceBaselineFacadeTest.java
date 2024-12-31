package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.common.enums.ResourceBaselineTypeEnum;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.application.ApplicationResourceBaselineParam;
import com.baiyi.cratos.domain.view.application.ApplicationResourceBaselineVO;
import com.baiyi.cratos.facade.application.ApplicationResourceBaselineFacade;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/30 16:05
 * &#064;Version 1.0
 */
public class ApplicationResourceBaselineFacadeTest extends BaseUnit {

    @Resource
    private ApplicationResourceBaselineFacade applicationResourceBaselineFacade;

    @Test
    void scanAllTest() {
        applicationResourceBaselineFacade.scanAll();
    }

    @Test
    void queryTest() {
        ApplicationResourceBaselineParam.BaselineMember byMemberType = ApplicationResourceBaselineParam.BaselineMember.builder()
                .baselineType(ResourceBaselineTypeEnum.CONTAINER_LIFECYCLE.name())
                .standard(true)
                .build();
        ApplicationResourceBaselineParam.ApplicationResourceBaselinePageQuery pageQuery = ApplicationResourceBaselineParam.ApplicationResourceBaselinePageQuery.builder()
                .byMemberType(byMemberType)
                .page(1)
                .length(10)
                .build();
        DataTable<ApplicationResourceBaselineVO.ResourceBaseline> dataTable = applicationResourceBaselineFacade.queryApplicationResourceBaselinePage(
                pageQuery);
        System.out.println(dataTable);
    }

}
