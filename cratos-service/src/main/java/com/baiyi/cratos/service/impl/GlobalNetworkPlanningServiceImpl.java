package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.GlobalNetworkPlanning;
import com.baiyi.cratos.domain.param.network.GlobalNetworkPlanningParam;
import com.baiyi.cratos.mapper.GlobalNetworkPlanningMapper;
import com.baiyi.cratos.service.GlobalNetworkPlanningService;
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
 * &#064;Date  2024/9/2 16:17
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.GLOBAL_NETWORK_PLANNING)
public class GlobalNetworkPlanningServiceImpl implements GlobalNetworkPlanningService {

    private final GlobalNetworkPlanningMapper globalNetworkPlanningMapper;

    @Override
    public GlobalNetworkPlanning getByUniqueKey(@NonNull GlobalNetworkPlanning record) {
        Example example = new Example(GlobalNetworkPlanning.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("networkId", record.getNetworkId())
                .andEqualTo("cidrBlock", record.getCidrBlock());
        return globalNetworkPlanningMapper.selectOneByExample(example);
    }

    @Override
    public DataTable<GlobalNetworkPlanning> queryGlobalNetworkPlanningPage(
            GlobalNetworkPlanningParam.GlobalNetworkPlanningPageQueryParam param) {
        Page<GlobalNetworkPlanning> page = PageHelper.startPage(param.getPage(), param.getLength());
        List<GlobalNetworkPlanning> data = globalNetworkPlanningMapper.queryPageByParam(param);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public List<GlobalNetworkPlanning> queryByNetworkId(int networkId) {
        Example example = new Example(GlobalNetworkPlanning.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("networkId", networkId);
        return globalNetworkPlanningMapper.selectByExample(example);
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:GLOBALNETWORKPLANNING:ID:' + #id")
    public void clearCacheById(int id) {
    }

}
