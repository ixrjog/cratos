package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.GlobalNetwork;
import com.baiyi.cratos.domain.generator.GlobalNetworkSubnet;
import com.baiyi.cratos.domain.param.network.GlobalNetworkParam;
import com.baiyi.cratos.mapper.GlobalNetworkMapper;
import com.baiyi.cratos.service.GlobalNetworkService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/2 17:27
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.GLOBAL_NETWORK)
public class GlobalNetworkServiceImpl implements GlobalNetworkService {

    private final GlobalNetworkMapper globalNetworkMapper;

    @Override
    public DataTable<GlobalNetwork> queryGlobalNetworkPage(GlobalNetworkParam.GlobalNetworkPageQueryParam param) {
        Page<GlobalNetwork> page = PageHelper.startPage(param.getPage(), param.getLength());
        List<GlobalNetwork> data = globalNetworkMapper.queryPageByParam(param);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public GlobalNetwork getByUniqueKey(@NonNull GlobalNetwork record) {
        Example example = new Example(GlobalNetwork.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", record.getName())
                .andEqualTo("cidrBlock", record.getCidrBlock());
        return globalNetworkMapper.selectOneByExample(example);
    }

    @Override
    public List<GlobalNetwork> queryByValid() {
        Example example = new Example(GlobalNetwork.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("valid", true);
        return globalNetworkMapper.selectByExample(example);
    }

}
