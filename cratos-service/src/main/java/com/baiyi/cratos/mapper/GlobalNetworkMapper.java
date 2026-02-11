package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.GlobalNetwork;
import com.baiyi.cratos.domain.param.http.network.GlobalNetworkParam;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface GlobalNetworkMapper extends Mapper<GlobalNetwork> {

    List<GlobalNetwork> queryPageByParam(GlobalNetworkParam.GlobalNetworkPageQueryParam pageQuery);

}