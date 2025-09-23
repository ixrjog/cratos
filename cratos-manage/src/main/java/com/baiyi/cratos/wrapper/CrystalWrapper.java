package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.view.crystal.CrystalServerVO;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.BaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/10/11 10:56
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CrystalWrapper extends BaseDataTableConverter<CrystalServerVO.AssetServer, EdsAsset> implements BaseWrapper<CrystalServerVO.AssetServer> {

    private final EdsAssetIndexService edsAssetIndexService;
    private final EdsAssetWrapper edsAssetWrapper;

    @Override
    public void wrap(CrystalServerVO.AssetServer vo) {
        edsAssetWrapper.wrap(vo.getAsset());
    }

    @Override
    public CrystalServerVO.AssetServer wrapToTarget(EdsAsset s) {
        EdsAssetVO.Asset vo = edsAssetWrapper.convert(s);
        return CrystalServerVO.AssetServer.builder()
                .asset(vo)
                .build()
                .init();
    }

}
