package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/3/29 10:25
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EdsAssetIndexWrapper extends BaseDataTableConverter<EdsAssetVO.Index, EdsAssetIndex> implements IBaseWrapper<EdsAssetVO.Index> {

    @Override
    public void wrap(EdsAssetVO.Index index) {
    }

}
