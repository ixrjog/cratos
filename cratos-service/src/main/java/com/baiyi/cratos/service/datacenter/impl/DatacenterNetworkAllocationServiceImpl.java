package com.baiyi.cratos.service.datacenter.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.DatacenterNetworkAllocation;
import com.baiyi.cratos.domain.param.http.datacenter.DatacenterNetworkParam;
import com.baiyi.cratos.mapper.DatacenterNetworkAllocationMapper;
import com.baiyi.cratos.service.datacenter.DatacenterNetworkAllocationService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/8 17:56
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.DATACENTER_NETWORK_ALLOCATION)
public class DatacenterNetworkAllocationServiceImpl implements DatacenterNetworkAllocationService {

    private final DatacenterNetworkAllocationMapper datacenterNetworkAllocationMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DATACENTER:NETWORK:ALLOCATION:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public DatacenterNetworkAllocation getByUniqueKey(@NonNull DatacenterNetworkAllocation record) {
        Example example = new Example(DatacenterNetworkAllocation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("networkId", record.getNetworkId())
                .andEqualTo("cidr", record.getCidr());
        return datacenterNetworkAllocationMapper.selectOneByExample(example);
    }

    @Override
    public DataTable<DatacenterNetworkAllocation> queryAllocationPage(
            DatacenterNetworkParam.AllocationPageQuery pageQuery) {
        Page<DatacenterNetworkAllocation> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<DatacenterNetworkAllocation> data = datacenterNetworkAllocationMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public List<DatacenterNetworkAllocation> queryOverlappingAllocations(long ipStart, long ipEnd, Integer excludeId) {
        return datacenterNetworkAllocationMapper.queryOverlapping(ipStart, ipEnd, excludeId);
    }

    @Override
    public List<DatacenterNetworkAllocation> queryAllocationsInRange(long ipStart, long ipEnd) {
        return datacenterNetworkAllocationMapper.queryOverlapping(ipStart, ipEnd, null);
    }

    @Override
    public List<DatacenterNetworkAllocation> queryByNetworkId(int networkId) {
        Example example = new Example(DatacenterNetworkAllocation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("networkId", networkId);
        return datacenterNetworkAllocationMapper.selectByExample(example);
    }

}
