package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.exception.DatacenterNetworkException;
import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.common.util.NetworkUtils;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.DatacenterNetwork;
import com.baiyi.cratos.domain.generator.DatacenterNetworkAllocation;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.http.datacenter.DatacenterNetworkParam;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.domain.view.datacenter.DatacenterVO;
import com.baiyi.cratos.eds.network.NetworkAllocator;
import com.baiyi.cratos.eds.network.NetworkAllocatorFactory;
import com.baiyi.cratos.eds.network.model.NetworkModel;
import com.baiyi.cratos.facade.DatacenterFacade;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.datacenter.DatacenterNetworkAllocationService;
import com.baiyi.cratos.service.datacenter.DatacenterNetworkService;
import com.baiyi.cratos.wrapper.datacenter.DatacenterNetworkAllocationWrapper;
import com.baiyi.cratos.wrapper.datacenter.DatacenterNetworkWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/8 17:11
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class DatacenterFacadeImpl implements DatacenterFacade {

    private final DatacenterNetworkService networkService;
    private final DatacenterNetworkAllocationService allocationService;
    private final DatacenterNetworkWrapper networkWrapper;
    private final DatacenterNetworkAllocationWrapper allocationWrapper;
    private final EdsInstanceService edsInstanceService;

    @Override
    public DataTable<DatacenterVO.Network> queryNetworkPage(DatacenterNetworkParam.NetworkPageQuery pageQuery) {
        DataTable<DatacenterNetwork> table = networkService.queryNetworkPage(pageQuery);
        return networkWrapper.wrapToTarget(table);
    }

    @Override
    public void addNetwork(DatacenterNetworkParam.AddNetwork addNetwork) {
        DatacenterNetwork network = addNetwork.toTarget();
        network.setValid(true);
        if (!StringUtils.hasText(addNetwork.getName())) {
            if (IdentityUtils.hasIdentity(addNetwork.getEdsInstanceId())) {
                EdsInstance instance = edsInstanceService.getById(addNetwork.getEdsInstanceId());
                network.setName(instance.getInstanceName());
            }
        }
        networkService.add(network);
    }

    @Override
    public void updateNetwork(DatacenterNetworkParam.UpdateNetwork updateNetwork) {
        networkService.updateByPrimaryKey(updateNetwork.toTarget());
    }

    @Override
    public DataTable<DatacenterVO.Allocation> queryAllocationPage(
            DatacenterNetworkParam.AllocationPageQuery pageQuery) {
        DataTable<DatacenterNetworkAllocation> table = allocationService.queryAllocationPage(pageQuery);
        return allocationWrapper.wrapToTarget(table);
    }

    @Override
    public void addAllocation(DatacenterNetworkParam.AddAllocation addAllocation) {
        if (!NetworkUtils.isCidr(addAllocation.getCidr())) {
            DatacenterNetworkException.runtime("Invalid CIDR format.");
        }
        if (!addAllocation.getAllowOverlap()) {
            DatacenterNetworkParam.CheckCidrConflict checkCidrConflict = DatacenterNetworkParam.CheckCidrConflict.builder()
                    .cidr(addAllocation.getCidr())
                    .build();
            DatacenterVO.CidrConflictResult result = checkCidrConflict(checkCidrConflict);
            if (result.isConflict()) {
                DatacenterNetworkException.runtime("CIDR conflict detected.");
            }
        }
        DatacenterNetworkAllocation allocation = addAllocation.toTarget();
        allocation.setValid(true);
        // 计算 IP 范围
        long[] range = NetworkUtils.cidrToRange(addAllocation.getCidr());
        allocation.setIpStart(range[0]);
        allocation.setIpEnd(range[1]);
        allocationService.add(allocation);
    }

    @Override
    public void updateAllocation(DatacenterNetworkParam.UpdateAllocation updateAllocation) {
        DatacenterNetworkAllocation allocation = updateAllocation.toTarget();
        long[] range = NetworkUtils.cidrToRange(updateAllocation.getCidr());
        allocation.setIpStart(range[0]);
        allocation.setIpEnd(range[1]);
        allocationService.updateByPrimaryKey(allocation);
    }

    @Override
    public void deleteAllocationById(int id) {
        allocationService.deleteById(id);
    }

    @Override
    public DatacenterVO.CidrConflictResult checkCidrConflict(
            DatacenterNetworkParam.CheckCidrConflict checkCidrConflict) {
        long[] range = NetworkUtils.cidrToRange(checkCidrConflict.getCidr());
        List<DatacenterNetworkAllocation> overlapping = allocationService.queryOverlappingAllocations(
                range[0], range[1], checkCidrConflict.getExcludeId());
        List<DatacenterVO.Allocation> conflictAllocations = overlapping.stream()
                .map(allocationWrapper::convert)
                .toList();
        return DatacenterVO.CidrConflictResult.builder()
                .conflict(!conflictAllocations.isEmpty())
                .conflictAllocations(conflictAllocations)
                .build();
    }

    @Override
    public DatacenterVO.AvailableCidrResult findAvailableCidrs(DatacenterNetworkParam.FindAvailableCidr param) {
        long[] parentRange = NetworkUtils.cidrToRange(param.getParentCidr());
        long parentStart = parentRange[0];
        long parentEnd = parentRange[1];
        int prefixLength = param.getPrefixLength();
        long subnetSize = 1L << (32 - prefixLength);
        int limit = param.getLimit();
        // Query all existing allocations in the parent range, sorted by ip_start
        List<DatacenterNetworkAllocation> existing = allocationService.queryAllocationsInRange(parentStart, parentEnd);
        // Build a set of occupied ranges
        List<long[]> occupied = existing.stream()
                .map(a -> new long[]{a.getIpStart(), a.getIpEnd()})
                .sorted(Comparator.comparingLong(a -> a[0]))
                .toList();
        List<String> available = new java.util.ArrayList<>();
        // Iterate through all possible subnet-aligned blocks
        long candidate = (parentStart + subnetSize - 1) / subnetSize * subnetSize; // align to subnet boundary
        if (candidate < parentStart) {
            candidate = parentStart;
        }
        while (candidate + subnetSize - 1 <= parentEnd && available.size() < limit) {
            long candidateEnd = candidate + subnetSize - 1;
            boolean conflict = false;
            for (long[] occ : occupied) {
                if (occ[0] <= candidateEnd && occ[1] >= candidate) {
                    conflict = true;
                    // Skip past this occupied range, align to next subnet boundary
                    candidate = ((occ[1] / subnetSize) + 1) * subnetSize;
                    break;
                }
            }
            if (!conflict) {
                available.add(NetworkUtils.longToIp(candidate) + "/" + prefixLength);
                candidate += subnetSize;
            }
        }
        return DatacenterVO.AvailableCidrResult.builder()
                .parentCidr(param.getParentCidr())
                .prefixLength(prefixLength)
                .availableCidrs(available)
                .build();
    }

    @Override
    @Async
    public void scanNetworkAllocation(int networkId) {
        DatacenterNetwork network = networkService.getById(networkId);
        if (!IdentityUtils.hasIdentity(network.getEdsInstanceId())) {
            // 未关联数据源
            return;
        }
        EdsInstance edsInstance = edsInstanceService.getById(network.getEdsInstanceId());
        if (edsInstance == null) {
            return;
        }
        NetworkAllocator networkAllocator = NetworkAllocatorFactory.getNetworkAllocator(edsInstance.getEdsType());
        if (networkAllocator == null) {
            return;
        }
        List<NetworkModel.Allocation> allocations = networkAllocator.queryNetworkAllocations(edsInstance.getId());
        if (CollectionUtils.isEmpty(allocations)) {
            return;
        }
        Map<String, DatacenterNetworkAllocation> networkAllocationMap = allocationService.queryByNetworkId(networkId)
                .stream()
                .collect(Collectors.toMap(DatacenterNetworkAllocation::getCidr, Function.identity()));
        allocations.forEach(allocation -> {
            String cidr = allocation.getCidr();
            if (!networkAllocationMap.containsKey(cidr)) {
                long[] range = NetworkUtils.cidrToRange(cidr);
                DatacenterNetworkAllocation datacenterNetworkAllocation = DatacenterNetworkAllocation.builder()
                        .networkId(networkId)
                        .name(allocation.getName())
                        .cidr(allocation.getCidr())
                        .region(allocation.getRegion())
                        .allocationType(allocation.getType())
                        .allowOverlap(false)
                        .ipStart(range[0])
                        .ipEnd(range[1])
                        .valid(true)
                        .comment(StringFormatter.arrayFormat(
                                "vpcId={}, subnetId={}", allocation.getVpcId(),
                                allocation.getSubnetId()
                        ))
                        .build();
                allocationService.add(datacenterNetworkAllocation);
            }
        });
    }

    @Override
    public DatacenterVO.SubnetMap getSubnetMap(String parentCidr, int prefixLength) {
        long[] parentRange = NetworkUtils.cidrToRange(parentCidr);
        long parentStart = parentRange[0];
        long parentEnd = parentRange[1];
        long subnetSize = 1L << (32 - prefixLength);

        List<DatacenterNetworkAllocation> existing = allocationService.queryAllocationsInRange(parentStart, parentEnd);
        // Build lookup: key = subnet start IP, value = list of allocations
        Map<Long, List<DatacenterNetworkAllocation>> allocMap = new HashMap<>();
        for (DatacenterNetworkAllocation alloc : existing) {
            long allocStart = Math.max(alloc.getIpStart(), parentStart);
            long allocEnd = Math.min(alloc.getIpEnd(), parentEnd);
            long blockStart = (allocStart / subnetSize) * subnetSize;
            while (blockStart <= allocEnd && blockStart + subnetSize - 1 <= parentEnd) {
                if (blockStart >= parentStart) {
                    allocMap.computeIfAbsent(blockStart, k -> new ArrayList<>()).add(alloc);
                }
                blockStart += subnetSize;
            }
        }

        int totalBlocks = (int) ((parentEnd - parentStart + 1) / subnetSize);
        int cols = (int) Math.ceil(Math.sqrt(totalBlocks));
        int rows = (int) Math.ceil((double) totalBlocks / cols);

        List<DatacenterVO.SubnetBlock> blocks = new ArrayList<>();
        LongStream.iterate(parentStart, ip -> ip + subnetSize - 1 <= parentEnd, ip -> ip + subnetSize)
                .forEach(ip -> {
                    List<DatacenterNetworkAllocation> allocs = allocMap.get(ip);
                    boolean allocated = allocs != null && !allocs.isEmpty();
                    DatacenterNetworkAllocation first = allocated ? allocs.get(0) : null;
                    List<String> types = allocated ? allocs.stream()
                            .map(DatacenterNetworkAllocation::getAllocationType)
                            .distinct()
                            .collect(Collectors.toList()) : List.of();
                    blocks.add(DatacenterVO.SubnetBlock.builder()
                                       .cidr(NetworkUtils.longToIp(ip) + "/" + prefixLength)
                                       .allocated(allocated)
                                       .allocationName(first != null ? first.getName() : null)
                                       .allocationType(first != null ? first.getAllocationType() : null)
                                       .allocationTypes(types)
                                       .build());
                });

        return DatacenterVO.SubnetMap.builder()
                .parentCidr(parentCidr)
                .prefixLength(prefixLength)
                .cols(cols)
                .rows(rows)
                .blocks(blocks)
                .build();
    }

    @Override
    public List<DatacenterVO.Allocation> queryAllocationsByCidr(String cidr) {
        long[] range = NetworkUtils.cidrToRange(cidr);
        return allocationService.queryAllocationsInRange(range[0], range[1]).stream()
                .map(allocationWrapper::convert)
                .peek(allocationWrapper::wrap)
                .toList();
    }

}
