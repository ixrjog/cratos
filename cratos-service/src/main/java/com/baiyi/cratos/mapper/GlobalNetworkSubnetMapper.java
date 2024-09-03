package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.GlobalNetworkSubnet;
import com.baiyi.cratos.domain.param.network.GlobalNetworkSubnetParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface GlobalNetworkSubnetMapper extends Mapper<GlobalNetworkSubnet> {

    List<GlobalNetworkSubnet> queryPageByParam(GlobalNetworkSubnetParam.GlobalNetworkSubnetPageQueryParam pageQuery);

}