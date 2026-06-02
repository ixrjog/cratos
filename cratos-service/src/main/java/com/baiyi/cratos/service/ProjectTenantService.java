package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.ProjectTenant;
import com.baiyi.cratos.domain.param.http.project.ProjectParam;
import com.baiyi.cratos.mapper.ProjectTenantMapper;
import com.baiyi.cratos.service.base.BaseValidService;

import java.util.List;

public interface ProjectTenantService extends BaseValidService<ProjectTenant, ProjectTenantMapper> {

    List<ProjectTenant> queryTenants(Integer projectId, String tenantCode, String countryCode);

    ProjectTenant getTenant(Integer projectId, String tenantCode);

    DataTable<ProjectTenant> queryProjectTenantPage(ProjectParam.ProjectTenantPageQueryParam param);

}
