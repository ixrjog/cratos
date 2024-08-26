package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.annotation.DeleteBoundBusiness;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Domain;
import com.baiyi.cratos.domain.generator.GlobalNetwork;
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
 * &#064;Date  2024/8/26 10:33
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.GLOBAL_NETWORK)
public class GlobalNetworkServiceImpl implements GlobalNetworkService {

    private final GlobalNetworkMapper globalNetworkMapper;

    @Override
    public GlobalNetwork getByUniqueKey(@NonNull GlobalNetwork record) {
        Example example = new Example(Domain.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cidrBlock", record.getCidrBlock());
        return globalNetworkMapper.selectOneByExample(example);
    }

    @Override
    public DataTable<GlobalNetwork> queryGlobalNetworkPage(GlobalNetworkParam.GlobalNetworkPageQueryParam param) {
        Page<GlobalNetwork> page = PageHelper.startPage(param.getPage(), param.getLength());
        List<GlobalNetwork> data = globalNetworkMapper.queryPageByParam(param);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    @DeleteBoundBusiness(businessId = "#id", targetTypes = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC})
    public void deleteById(int id) {
        globalNetworkMapper.deleteByPrimaryKey(id);
    }

}
