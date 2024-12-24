package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.generator.ApplicationActuator;
import com.baiyi.cratos.domain.param.http.application.ApplicationActuatorParam;
import com.baiyi.cratos.mapper.ApplicationActuatorMapper;
import com.baiyi.cratos.service.ApplicationActuatorService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/23 15:49
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
public class ApplicationActuatorServiceImpl implements ApplicationActuatorService {

    private final ApplicationActuatorMapper applicationActuatorMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:APPLICATION_ACTUATOR:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public ApplicationActuator getByUniqueKey(@NonNull ApplicationActuator record) {
        Example example = new Example(ApplicationActuator.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("applicationName", record.getApplicationName())
                .andEqualTo("businessType", record.getBusinessType())
                .andEqualTo("businessId", record.getBusinessId());
        return applicationActuatorMapper.selectOneByExample(example);
    }

    @Override
    public DataTable<ApplicationActuator> queryApplicationActuatorPage(
            ApplicationActuatorParam.ApplicationActuatorPageQuery pageQuery) {
        Page<ApplicationActuator> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<ApplicationActuator> data = applicationActuatorMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

}
