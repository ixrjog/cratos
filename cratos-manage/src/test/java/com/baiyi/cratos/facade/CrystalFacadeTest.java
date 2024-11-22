package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.domain.view.crystal.CrystalServerVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/10/9 16:25
 * &#064;Version 1.0
 */
public class CrystalFacadeTest extends BaseUnit {

    @Resource
    private CrystalFacade crystalFacade;

    @Test
    void test1() {
        OptionsVO.Options options = crystalFacade.getInstanceAssetTypeOptions(EdsInstanceTypeEnum.ALIYUN.name());
        System.out.println(options);
    }

    @Test
    void test2() {
        EdsInstanceParam.AssetPageQuery assetPageQuery = EdsInstanceParam.AssetPageQuery.builder()
                .instanceId(93)
                .assetType(EdsAssetTypeEnum.ALIYUN_ECS.name())
                .page(1)
                .length(5)
                .build();
        DataTable<CrystalServerVO.AssetServer> table = crystalFacade.queryEdsInstanceAssetPage(assetPageQuery);
        System.out.println(table);
    }

}