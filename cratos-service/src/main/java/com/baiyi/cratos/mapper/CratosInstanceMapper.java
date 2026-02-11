package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.CratosInstance;
import com.baiyi.cratos.domain.param.http.cratos.CratosInstanceParam;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface CratosInstanceMapper extends Mapper<CratosInstance> {

    List<CratosInstance> queryPageByParam(CratosInstanceParam.RegisteredInstancePageQueryParam param);

}