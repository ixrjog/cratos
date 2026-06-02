package com.baiyi.cratos.service.impl;


import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.ProjectTenant;
import com.baiyi.cratos.domain.param.http.project.ProjectParam;
import com.baiyi.cratos.mapper.ProjectTenantMapper;
import com.baiyi.cratos.service.ProjectTenantService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

@Service
@RequiredArgsConstructor
public class ProjectTenantServiceImpl implements ProjectTenantService {

    private final ProjectTenantMapper projectTenantMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:PROJECT_TENANT:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public List<ProjectTenant> queryTenants(Integer projectId, String tenantCode, String countryCode) {
        Example example = new Example(ProjectTenant.class);
        Example.Criteria criteria = example.createCriteria();
        if (IdentityUtils.hasIdentity(projectId)) {
            criteria.andEqualTo("projectId", projectId);
        }
        criteria.andEqualTo("tenantCode", tenantCode);
        if (StringUtils.hasText(countryCode)) {
            criteria.andEqualTo("countryCode", countryCode);
        }
        return projectTenantMapper.selectByExample(example);
    }

    @Override
    public ProjectTenant getTenant(Integer projectId, String tenantCode) {
        Example example = new Example(ProjectTenant.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId", projectId)
                .andEqualTo("tenantCode", tenantCode);
        return projectTenantMapper.selectOneByExample(example);
    }

    @Override
    public DataTable<ProjectTenant> queryProjectTenantPage(ProjectParam.ProjectTenantPageQueryParam param) {
        Page<ProjectTenant> page = PageHelper.startPage(param.getPage(), param.getLength());
        List<ProjectTenant> data = projectTenantMapper.queryPageByParam(param);
        return new DataTable<>(data, page.getTotal());
    }

}
