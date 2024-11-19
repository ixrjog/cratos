package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.annotation.DeleteBoundBusiness;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.GlobalNetworkSubnet;
import com.baiyi.cratos.domain.param.network.GlobalNetworkSubnetParam;
import com.baiyi.cratos.mapper.GlobalNetworkSubnetMapper;
import com.baiyi.cratos.service.GlobalNetworkSubnetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/26 10:33
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.GLOBAL_NETWORK_SUBNET)
public class GlobalNetworkSubnetServiceImpl implements GlobalNetworkSubnetService {

    private final GlobalNetworkSubnetMapper globalNetworkSubnetMapper;

    @Override
    public GlobalNetworkSubnet getByUniqueKey(@NonNull GlobalNetworkSubnet record) {
        Example example = new Example(GlobalNetworkSubnet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cidrBlock", record.getCidrBlock());
        return globalNetworkSubnetMapper.selectOneByExample(example);
    }

    @Override
    public DataTable<GlobalNetworkSubnet> queryGlobalNetworkSubnetPage(
            GlobalNetworkSubnetParam.GlobalNetworkSubnetPageQueryParam param) {
        Page<GlobalNetworkSubnet> page = PageHelper.startPage(param.getPage(), param.getLength());
        List<GlobalNetworkSubnet> data = globalNetworkSubnetMapper.queryPageByParam(param);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public List<GlobalNetworkSubnet> queryByValid() {
        Example example = new Example(GlobalNetworkSubnet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("valid", true);
        return globalNetworkSubnetMapper.selectByExample(example);
    }

    @Override
    @DeleteBoundBusiness(businessId = "#id", targetTypes = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC})
    public void deleteById(int id) {
        GlobalNetworkSubnetService.super.deleteById(id);
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:GLOBALNETWORKSUBNET:ID:' + #id")
    public void clearCacheById(int id) {
    }

}
