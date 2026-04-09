package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.DatacenterNetworkAllocation;
import com.baiyi.cratos.domain.param.http.datacenter.DatacenterNetworkParam;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface DatacenterNetworkAllocationMapper extends Mapper<DatacenterNetworkAllocation> {

    List<DatacenterNetworkAllocation> queryPageByParam(DatacenterNetworkParam.AllocationPageQuery pageQuery);

    List<DatacenterNetworkAllocation> queryOverlapping(@org.apache.ibatis.annotations.Param("ipStart") long ipStart,
                                                       @org.apache.ibatis.annotations.Param("ipEnd") long ipEnd,
                                                       @org.apache.ibatis.annotations.Param("excludeId") Integer excludeId);

}