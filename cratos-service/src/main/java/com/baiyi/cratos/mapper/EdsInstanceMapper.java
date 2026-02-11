package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface EdsInstanceMapper extends Mapper<EdsInstance> {

    List<EdsInstance> queryPageByParam(EdsInstanceParam.InstancePageQueryParam param);

}