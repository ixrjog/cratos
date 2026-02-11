package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.GlobalNetworkSubnet;
import com.baiyi.cratos.domain.param.http.network.GlobalNetworkSubnetParam;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface GlobalNetworkSubnetMapper extends Mapper<GlobalNetworkSubnet> {

    List<GlobalNetworkSubnet> queryPageByParam(GlobalNetworkSubnetParam.GlobalNetworkSubnetPageQueryParam pageQuery);

}