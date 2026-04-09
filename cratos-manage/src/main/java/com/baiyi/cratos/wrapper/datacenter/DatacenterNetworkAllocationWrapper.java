package com.baiyi.cratos.wrapper.datacenter;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.DatacenterNetwork;
import com.baiyi.cratos.domain.generator.DatacenterNetworkAllocation;
import com.baiyi.cratos.domain.view.datacenter.DatacenterVO;
import com.baiyi.cratos.service.datacenter.DatacenterNetworkAllocationService;
import com.baiyi.cratos.service.datacenter.DatacenterNetworkService;
import com.baiyi.cratos.wrapper.base.BaseBusinessDecorator;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/8 20:25
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.DATACENTER_NETWORK_ALLOCATION)
public class DatacenterNetworkAllocationWrapper extends BaseDataTableConverter<DatacenterVO.Allocation, DatacenterNetworkAllocation> implements BaseBusinessDecorator<DatacenterVO.HasNetworkAllocations, DatacenterVO.Allocation> {

    private final DatacenterNetworkService networkService;
    private final DatacenterNetworkAllocationService allocationService;

    @Override
    public void wrap(DatacenterVO.Allocation vo) {
        DatacenterNetwork network = networkService.getById(vo.getNetworkId());
        vo.setNetworkName(network.getName());
    }

    @Override
    public void decorateBusiness(DatacenterVO.HasNetworkAllocations hasBusiness) {
        List<DatacenterNetworkAllocation> allocations = allocationService.queryByNetworkId(hasBusiness.getNetworkId());
        hasBusiness.setAllocations(allocations.stream()
                                           .map(this::wrapToTarget)
                                           .sorted(Comparator.comparing(DatacenterVO.Allocation::getIpStart))
                                           .toList()
        );
    }

}