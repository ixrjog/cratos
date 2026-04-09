package com.baiyi.cratos.wrapper.datacenter;

import com.baiyi.cratos.annotation.BusinessDecorator;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.DatacenterNetwork;
import com.baiyi.cratos.domain.view.datacenter.DatacenterVO;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.BaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/8 18:14
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DatacenterNetworkWrapper extends BaseDataTableConverter<DatacenterVO.Network, DatacenterNetwork> implements BaseWrapper<DatacenterVO.Network> {

    @Override
    @BusinessDecorator(types = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC, BusinessTypeEnum.EDS_INSTANCE, BusinessTypeEnum.ACCOUNT_ENTITY, BusinessTypeEnum.DATACENTER_NETWORK_ALLOCATION})
    public void wrap(DatacenterVO.Network vo) {
    }

}