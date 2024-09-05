package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.GlobalNetworkPlanning;
import com.baiyi.cratos.domain.view.network.GlobalNetworkVO;
import com.baiyi.cratos.service.GlobalNetworkPlanningService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBusinessWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/3 11:27
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.GLOBAL_NETWORK_PLANNING)
public class GlobalNetworkPlanningWrapper extends BaseDataTableConverter<GlobalNetworkVO.Planning, GlobalNetworkPlanning> implements IBusinessWrapper<GlobalNetworkVO.HasPlannings, GlobalNetworkVO.Planning> {

    private final GlobalNetworkPlanningService globalNetworkPlanningService;

    @Override
    @BusinessWrapper(ofTypes = {BusinessTypeEnum.GLOBAL_NETWORK, BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC})
    public void wrap(GlobalNetworkVO.Planning vo) {
    }

    @Override
    public void businessWrap(GlobalNetworkVO.HasPlannings hasPlannings) {
        List<GlobalNetworkPlanning> globalNetworkPlannings = globalNetworkPlanningService.queryByNetworkId(
                hasPlannings.getNetworkId());
        hasPlannings.setPlannings(globalNetworkPlannings.stream()
                .map(this::convert)
                .collect(Collectors.toList()));
    }

}