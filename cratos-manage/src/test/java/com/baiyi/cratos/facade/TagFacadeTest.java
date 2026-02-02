package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.domain.param.http.tag.TagGroupParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.facade.tag.TagGroupFacade;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/2 15:39
 * &#064;Version 1.0
 */
public class TagFacadeTest extends BaseUnit {

    @Resource
    private TagGroupFacade tagGroupFacade;

    @Resource
    private BusinessTagFacade businessTagFacade;

    @Test
    void test() {
        TagGroupParam.GroupAssetPageQuery pageQuery = TagGroupParam.GroupAssetPageQuery.builder()
                .tagGroup("tms-itel")
                .page(1)
                .length(10)
                .build();
        DataTable<EdsAssetVO.Asset> dataTable = tagGroupFacade.queryGroupAssetPage(pageQuery);
        System.out.println(dataTable);
    }

    @Test
    void test1() {
        List<BusinessTag> businessTags = businessTagFacade.getCountryCodeBusinessTags();
        System.out.println(businessTags);
    }

}
