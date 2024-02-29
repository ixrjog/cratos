package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.domain.view.eds.EdsConfigVO;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/2/5 18:08
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EdsConfigWrapper extends BaseDataTableConverter<EdsConfigVO.EdsConfig, EdsConfig> implements IBaseWrapper<EdsConfigVO.EdsConfig> {

    private final EdsConfigService edsConfigService;

    @Override
    @BusinessWrapper(ofTypes = BusinessTypeEnum.CREDENTIAL)
    public void wrap(EdsConfigVO.EdsConfig edsConfig) {
        // Eds Instance Registered
    }

    public void wrap(EdsConfigVO.IEdsConfig target) {
        EdsConfig edsConfig = edsConfigService.getById(target.getConfigId());
        if (edsConfig != null) {
            target.setEdsConfig(this.wrapToTarget(edsConfig));
        }
    }

}
