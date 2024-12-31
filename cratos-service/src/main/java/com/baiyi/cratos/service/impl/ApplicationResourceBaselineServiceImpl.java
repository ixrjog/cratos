package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.ApplicationResourceBaseline;
import com.baiyi.cratos.domain.param.http.application.ApplicationResourceBaselineParam;
import com.baiyi.cratos.mapper.ApplicationResourceBaselineMapper;
import com.baiyi.cratos.service.ApplicationResourceBaselineService;
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
 * &#064;Date  2024/12/30 10:01
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
public class ApplicationResourceBaselineServiceImpl implements ApplicationResourceBaselineService {

    private final ApplicationResourceBaselineMapper applicationResourceBaselineMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:APPLICATION_RESOURCE_BASELINE:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public ApplicationResourceBaseline getByUniqueKey(@NonNull ApplicationResourceBaseline record) {
        Example example = new Example(ApplicationResourceBaseline.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("applicationName", record.getApplicationName())
                .andEqualTo("businessType", record.getBusinessType())
                .andEqualTo("businessId", record.getBusinessId());
        return applicationResourceBaselineMapper.selectOneByExample(example);
    }

    @Override
    public DataTable<ApplicationResourceBaseline> queryApplicationResourceBaselinePage(
            ApplicationResourceBaselineParam.ApplicationResourceBaselinePageQuery pageQuery) {
        Page<ApplicationResourceBaseline> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<ApplicationResourceBaseline> data = applicationResourceBaselineMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

}
