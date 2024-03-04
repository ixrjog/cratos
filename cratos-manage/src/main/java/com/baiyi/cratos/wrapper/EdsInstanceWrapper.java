package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.eds.core.EdsInstanceProviderFactory;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/2/5 17:22
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EdsInstanceWrapper extends BaseDataTableConverter<EdsInstanceVO.EdsInstance, EdsInstance> implements IBaseWrapper<EdsInstanceVO.EdsInstance> {

    private final EdsConfigWrapper edsConfigWrapper;

    @Override
    public void wrap(EdsInstanceVO.EdsInstance edsInstance) {
        // Eds Instance Registered
        edsInstance.setRegistered(edsInstance.getConfigId() != null);
        // Wrap Eds Config
        edsConfigWrapper.wrap(edsInstance);
        edsInstance.setAssetTypes(EdsInstanceProviderFactory.getInstanceAssetTypes(edsInstance.getEdsType()));
    }

}
