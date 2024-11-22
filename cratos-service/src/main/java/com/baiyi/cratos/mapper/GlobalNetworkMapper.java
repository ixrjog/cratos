package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.GlobalNetwork;
import com.baiyi.cratos.domain.param.http.network.GlobalNetworkParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface GlobalNetworkMapper extends Mapper<GlobalNetwork> {

    List<GlobalNetwork> queryPageByParam(GlobalNetworkParam.GlobalNetworkPageQueryParam pageQuery);

}