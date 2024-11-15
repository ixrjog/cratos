package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.generator.ApplicationResource;
import com.baiyi.cratos.mapper.ApplicationResourceMapper;
import com.baiyi.cratos.service.ApplicationResourceService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/15 13:36
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
public class ApplicationResourceServiceImpl implements ApplicationResourceService {

    private final ApplicationResourceMapper applicationResourceMapper;

    @Override
    public List<ApplicationResource> queryByApplicationName(String applicationName) {
        Example example = new Example(ApplicationResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("applicationName", applicationName);
        return applicationResourceMapper.selectByExample(example);
    }

    @Override
    public ApplicationResource getByUniqueKey(@NonNull ApplicationResource record) {
        Example example = new Example(ApplicationResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("applicationName", record.getApplicationName())
                .andEqualTo("businessType", record.getBusinessType())
                .andEqualTo("businessId", record.getBusinessId());
        return applicationResourceMapper.selectOneByExample(example);
    }

}
