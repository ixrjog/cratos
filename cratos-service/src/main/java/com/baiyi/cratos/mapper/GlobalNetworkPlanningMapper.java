package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.GlobalNetworkPlanning;
import com.baiyi.cratos.domain.param.http.network.GlobalNetworkPlanningParam;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface GlobalNetworkPlanningMapper extends Mapper<GlobalNetworkPlanning> {

    List<GlobalNetworkPlanning> queryPageByParam(GlobalNetworkPlanningParam.GlobalNetworkPlanningPageQueryParam pageQuery);

}