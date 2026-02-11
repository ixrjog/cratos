package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.domain.param.http.eds.EdsConfigParam;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface EdsConfigMapper extends Mapper<EdsConfig> {

    List<EdsConfig> queryPageByParam(EdsConfigParam.EdsConfigPageQuery pageQuery);

}