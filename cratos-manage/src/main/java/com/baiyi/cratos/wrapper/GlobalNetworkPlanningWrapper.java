package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.GlobalNetwork;
import com.baiyi.cratos.domain.generator.GlobalNetworkPlanning;
import com.baiyi.cratos.domain.util.BeanCopierUtils;
import com.baiyi.cratos.domain.view.network.GlobalNetworkVO;
import com.baiyi.cratos.service.GlobalNetworkPlanningService;
import com.baiyi.cratos.service.GlobalNetworkService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.BaseBusinessWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/3 11:27
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.GLOBAL_NETWORK_PLANNING)
public class GlobalNetworkPlanningWrapper extends BaseDataTableConverter<GlobalNetworkVO.Planning, GlobalNetworkPlanning> implements BaseBusinessWrapper<GlobalNetworkVO.HasPlannings, GlobalNetworkVO.Planning> {

    private final GlobalNetworkPlanningService globalNetworkPlanningService;
    private final GlobalNetworkService globalNetworkService;

    @Override
    @BusinessWrapper(types = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC})
    public void wrap(GlobalNetworkVO.Planning vo) {
        // 不使用BusinessWrapper避免循环注入
        GlobalNetwork globalNetwork = globalNetworkService.getById(vo.getNetworkId());
        if (globalNetwork != null) {
            vo.setNetwork(BeanCopierUtils.copyProperties(globalNetwork, GlobalNetworkVO.Network.class));
        }
    }

    @Override
    public void decorateBusiness(GlobalNetworkVO.HasPlannings biz) {
        List<GlobalNetworkPlanning> globalNetworkPlannings = globalNetworkPlanningService.queryByNetworkId(
                biz.getNetworkId());
        biz.setPlannings(globalNetworkPlannings.stream()
                .map(this::convert)
                .toList());
    }

}