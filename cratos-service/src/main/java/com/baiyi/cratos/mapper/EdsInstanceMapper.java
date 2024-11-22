package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface EdsInstanceMapper extends Mapper<EdsInstance> {

    List<EdsInstance> queryPageByParam(EdsInstanceParam.InstancePageQuery pageQuery);

}