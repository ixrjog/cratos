package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.generator.ProjectGroup;
import com.baiyi.cratos.mapper.ProjectGroupMapper;
import com.baiyi.cratos.service.base.BaseValidService;

import java.util.List;

public interface ProjectGroupService extends BaseValidService<ProjectGroup, ProjectGroupMapper> {

    List<ProjectGroup> queryByProjectId(int projectId);

    List<ProjectGroup> queryByTenantId(int tenantId);

}
