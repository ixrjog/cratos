package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.ApiSecurityTestRecord;
import com.baiyi.cratos.domain.param.http.security.ApiTestParam;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface ApiSecurityTestRecordMapper extends Mapper<ApiSecurityTestRecord> {
    List<ApiSecurityTestRecord> queryPageByParam(ApiTestParam.RecordPageQuery pageQuery);
}
