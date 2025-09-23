package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.GlobalNetwork;
import com.baiyi.cratos.domain.view.network.GlobalNetworkVO;
import com.baiyi.cratos.service.GlobalNetworkService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.BaseBusinessWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/3 13:47
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.GLOBAL_NETWORK)
public class GlobalNetworkWrapper extends BaseDataTableConverter<GlobalNetworkVO.Network, GlobalNetwork> implements BaseBusinessWrapper<GlobalNetworkVO.HasNetwork, GlobalNetworkVO.Network> {

    private final GlobalNetworkService globalNetworkService;

    @Override
    @BusinessWrapper(types = {BusinessTypeEnum.GLOBAL_NETWORK_PLANNING, BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC})
    public void wrap(GlobalNetworkVO.Network vo) {
    }

    @Override
    public void businessWrap(GlobalNetworkVO.HasNetwork hasNetwork) {
        if (hasNetwork.getNetworkId() <= 0) {
            return;
        }
        GlobalNetwork globalNetwork = globalNetworkService.getById(hasNetwork.getNetworkId());
        if (globalNetwork == null) {
            return;
        }
        hasNetwork.setNetwork(this.wrapToTarget(globalNetwork));
    }

}