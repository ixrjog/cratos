package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.ApiSecurityTestRecord;
import com.baiyi.cratos.domain.param.http.security.ApiTestParam;
import com.baiyi.cratos.mapper.ApiSecurityTestRecordMapper;
import com.baiyi.cratos.service.ApiSecurityTestRecordService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiSecurityTestRecordServiceImpl implements ApiSecurityTestRecordService {

    private final ApiSecurityTestRecordMapper apiSecurityTestRecordMapper;

    @Override
    public DataTable<ApiSecurityTestRecord> queryRecordPage(ApiTestParam.RecordPageQuery pageQuery) {
        Page<ApiSecurityTestRecord> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<ApiSecurityTestRecord> data = apiSecurityTestRecordMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal(), pageQuery);
    }

    @Override
    public void clearCacheById(int id) {
    }

}
