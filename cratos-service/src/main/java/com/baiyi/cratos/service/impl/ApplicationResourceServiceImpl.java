package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.common.configuration.CachingConfiguration;
import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.generator.ApplicationResource;
import com.baiyi.cratos.mapper.ApplicationResourceMapper;
import com.baiyi.cratos.service.ApplicationResourceService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

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
    @Cacheable(cacheNames = CachingConfiguration.RepositoryName.LONG_TERM, key = "'DOMAIN:APPLICATIONRESOURCE:APPLICATIONNAME:' + #applicationName", unless = "#result == null")
    public List<ApplicationResource> queryByApplicationName(String applicationName) {
        Example example = new Example(ApplicationResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("applicationName", applicationName);
        return applicationResourceMapper.selectByExample(example);
    }

    @Override
    public List<ApplicationResource> queryApplicationResource(String applicationName, String resourceType,
                                                              String namespace) {
        Example example = new Example(ApplicationResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("applicationName", applicationName)
                .andEqualTo("resourceType", resourceType)
                .andEqualTo("namespace", namespace);
        return applicationResourceMapper.selectByExample(example);
    }

    @Override
    public List<ApplicationResource> queryApplicationResource(String applicationName, String resourceType) {
        Example example = new Example(ApplicationResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("applicationName", applicationName)
                .andEqualTo("resourceType", resourceType);
        return applicationResourceMapper.selectByExample(example);
    }

    @Override
    public List<ApplicationResource> queryByBusiness(BaseBusiness.HasBusiness byBusiness) {
        Example example = new Example(ApplicationResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", byBusiness.getBusinessType())
                .andEqualTo("businessId", byBusiness.getBusinessId());
        return applicationResourceMapper.selectByExample(example);
    }

    @Override
    public List<ApplicationResource> queryByParam(String applicationName, String instanceName, String resourceType,
                                                  String namespace) {
        Example example = new Example(ApplicationResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("applicationName", applicationName)
                .andEqualTo("instanceName", instanceName)
                .andEqualTo("resourceType", resourceType)
                .andEqualTo("namespace", namespace);
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

    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:APPLICATIONRESOURCE:APPLICATIONNAME:' + #applicationName")
    public void clear(String applicationName) {
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:APPLICATIONRESOURCE:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public void add(ApplicationResource record) {
        ((ApplicationResourceService) AopContext.currentProxy()).clear(record.getApplicationName());
        ApplicationResourceService.super.add(record);
    }

    @Override
    public void updateByPrimaryKey(ApplicationResource record) {
        ((ApplicationResourceService) AopContext.currentProxy()).clear(record.getApplicationName());
        ApplicationResourceService.super.updateByPrimaryKey(record);
    }

    @Override
    public void deleteById(int id) {
        ApplicationResource record = getById(id);
        if (record != null) {
            ((ApplicationResourceService) AopContext.currentProxy()).clear(record.getApplicationName());
            ApplicationResourceService.super.deleteById(id);
        }
    }

    @Override
    public List<String> getNamespaceOptions() {
        return applicationResourceMapper.getNamespaceOptions();
    }

}
