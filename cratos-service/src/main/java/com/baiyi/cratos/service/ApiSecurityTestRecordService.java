package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.ApiSecurityTestRecord;
import com.baiyi.cratos.domain.param.http.security.ApiTestParam;
import com.baiyi.cratos.mapper.ApiSecurityTestRecordMapper;
import com.baiyi.cratos.service.base.BaseService;

public interface ApiSecurityTestRecordService extends BaseService<ApiSecurityTestRecord, ApiSecurityTestRecordMapper> {

    DataTable<ApiSecurityTestRecord> queryRecordPage(ApiTestParam.RecordPageQuery pageQuery);

}
