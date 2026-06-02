package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Project;
import com.baiyi.cratos.domain.param.http.project.ProjectParam;
import com.baiyi.cratos.mapper.ProjectMapper;
import com.baiyi.cratos.service.base.BaseValidService;

public interface ProjectService extends BaseValidService<Project, ProjectMapper> {

    Project getByProjectKey(String projectKey);

    DataTable<Project> queryProjectPage(ProjectParam.ProjectPageQueryParam param);

}
