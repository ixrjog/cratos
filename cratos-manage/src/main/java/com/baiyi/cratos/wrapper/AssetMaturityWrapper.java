package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.AssetMaturity;
import com.baiyi.cratos.domain.view.asset.AssetMaturityVO;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/30 下午2:51
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AssetMaturityWrapper extends BaseDataTableConverter<AssetMaturityVO.AssetMaturity, AssetMaturity> implements IBaseWrapper<AssetMaturityVO.AssetMaturity> {

    @Override
    @BusinessWrapper(types = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC})
    public void wrap(AssetMaturityVO.AssetMaturity vo) {
    }

}