package com.baiyi.cratos.service.impl;


import com.baiyi.cratos.domain.generator.ApiScanResult;
import com.baiyi.cratos.mapper.ApiScanResultMapper;
import com.baiyi.cratos.service.ApiScanResultService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
@RequiredArgsConstructor
public class ApiScanResultServiceImpl implements ApiScanResultService {

    private final ApiScanResultMapper apiScanResultMapper;

    @Override
    public void clearCacheById(int id) {
    }

    @Override
    public ApiScanResult getByUniqueKey(@NonNull ApiScanResult record) {
        Example example = new Example(ApiScanResult .class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("appName", record.getAppName())
                .andEqualTo("path", record.getPath());
        return apiScanResultMapper.selectOneByExample(example);
    }

}
