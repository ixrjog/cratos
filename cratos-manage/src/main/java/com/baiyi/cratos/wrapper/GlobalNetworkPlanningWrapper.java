package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.GlobalNetworkPlanning;
import com.baiyi.cratos.domain.view.network.GlobalNetworkVO;
import com.baiyi.cratos.service.GlobalNetworkPlanningService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
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
public class GlobalNetworkPlanningWrapper extends BaseDataTableConverter<GlobalNetworkVO.Planning, GlobalNetworkPlanning> implements IBaseWrapper<GlobalNetworkVO.Planning> {

    private final GlobalNetworkPlanningService globalNetworkPlanningService;

    @Override
    @BusinessWrapper(ofTypes = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC})
    public void wrap(GlobalNetworkVO.Planning vo) {
    }

    public void wrap(GlobalNetworkVO.HasPlannings hasPlannings) {
        List<GlobalNetworkPlanning> globalNetworkPlannings = globalNetworkPlanningService.queryByNetworkId(
                hasPlannings.getNetworkId());
        hasPlannings.setPlannings(globalNetworkPlannings.stream()
                .map(this::wrapToTarget)
                .collect(Collectors.toList()));
    }

}