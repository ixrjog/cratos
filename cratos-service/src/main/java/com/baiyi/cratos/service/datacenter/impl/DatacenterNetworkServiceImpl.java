package com.baiyi.cratos.service.datacenter.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.DatacenterNetwork;
import com.baiyi.cratos.domain.param.http.datacenter.DatacenterNetworkParam;
import com.baiyi.cratos.mapper.DatacenterNetworkMapper;
import com.baiyi.cratos.service.datacenter.DatacenterNetworkService;
import com.baiyi.cratos.util.SqlUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.NonNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/8 17:08
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.DATACENTER_NETWORK)
public class DatacenterNetworkServiceImpl implements DatacenterNetworkService {

    private final DatacenterNetworkMapper datacenterNetworkMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DATACENTER:NETWORK:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public DatacenterNetwork getByUniqueKey(@NonNull DatacenterNetwork record) {
        return null;
    }

    @Override
    public DataTable<DatacenterNetwork> queryNetworkPage(DatacenterNetworkParam.NetworkPageQuery pageQuery) {
        Page<DatacenterNetwork> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(DatacenterNetwork.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(pageQuery.getQueryName())) {
            criteria.andLike("name", SqlUtils.ofLike(pageQuery.getQueryName()));
        }
        List<DatacenterNetwork> data = datacenterNetworkMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

}
