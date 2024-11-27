package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.annotation.DeleteBoundBusiness;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.param.http.application.ApplicationParam;
import com.baiyi.cratos.mapper.ApplicationMapper;
import com.baiyi.cratos.service.ApplicationService;
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
 * &#064;Date  2024/11/15 11:18
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.APPLICATION)
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationMapper applicationMapper;

    @Override
    public DataTable<Application> queryApplicationPage(ApplicationParam.ApplicationPageQueryParam param) {
        Page<Application> page = PageHelper.startPage(param.getPage(), param.getLength());
        List<Application> data = applicationMapper.queryPageByParam(param);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public Application getByName(String name) {
        Application uniqueKey = Application.builder()
                .name(name)
                .build();
        return getByUniqueKey(uniqueKey);
    }

    @Override
    public Application getByUniqueKey(@NonNull Application record) {
        Example example = new Example(Application.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", record.getName());
        return applicationMapper.selectOneByExample(example);
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:APPLICATION:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    @DeleteBoundBusiness(businessId = "#id", targetTypes = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC})
    public void deleteById(int id) {
        ApplicationService.super.deleteById(id);
    }

}
