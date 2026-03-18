package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.tag.TagGroupParam;
import com.baiyi.cratos.domain.util.JSONUtils;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.facade.tag.TagGroupFacade;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/18 14:22
 * &#064;Version 1.0
 */
public class TagGroupFacadeTest extends BaseUnit {

    @Resource
    private TagGroupFacade tagGroupFacade;

    @Test
    void test1() {
        SessionUtils.setUsername("baiyi");
        TagGroupParam.MyGroupAssetPageQuery pageQuery = TagGroupParam.MyGroupAssetPageQuery.builder()
                .queryName("rocket")
                .tagGroup("rock")
                .page(1)
                .length(2)
                .build();

        DataTable<EdsAssetVO.Asset> dataTable = tagGroupFacade.queryMyGroupAssetPage(pageQuery);

        System.out.println(JSONUtils.writeValueAsPrettyString(dataTable));


    }
}
