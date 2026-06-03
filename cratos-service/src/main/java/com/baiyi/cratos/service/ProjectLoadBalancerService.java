package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.generator.ProjectLoadBalancer;
import com.baiyi.cratos.mapper.ProjectLoadBalancerMapper;
import com.baiyi.cratos.service.base.BaseValidService;

import java.util.List;

public interface ProjectLoadBalancerService extends BaseValidService<ProjectLoadBalancer, ProjectLoadBalancerMapper> {

    List<ProjectLoadBalancer> queryByProjectId(int projectId);

    List<ProjectLoadBalancer> queryByTenantId(int tenantId);

}
