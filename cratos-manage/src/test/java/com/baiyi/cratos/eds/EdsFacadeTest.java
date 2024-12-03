package com.baiyi.cratos.eds;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.facade.EdsFacade;
import com.baiyi.cratos.service.EdsAssetService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/29 15:02
 * &#064;Version 1.0
 */
public class EdsFacadeTest extends BaseUnit {

    @Resource
    private EdsFacade edsFacade;

    @Resource
    private EdsAssetService edsAssetService;

    @Test
    void test() {
        List<EdsAssetVO.Index> indices = edsFacade.queryAssetIndexByAssetId(47434);
        System.out.println(indices);
    }

    @Test
    void test1() {
//        List<EdsAsset> assets1 = edsAssetService.queryInstanceAssets(106);
//        assets1.forEach(e -> edsAssetService.deleteById(e.getId()));
    }

}
