package com.baiyi.cratos.facade.project;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.project.ProjectParam;
import com.baiyi.cratos.domain.view.project.ProjectVO;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/5/26 11:17
 * &#064;Version 1.0
 */
public interface ProjectFacade {

    ProjectVO.TenantView queryProjectTenantView(ProjectParam.ProjectTenantViewQuery queryParam);

    List<ProjectVO.Tenant> queryProjectTenants(String projectKey);

    DataTable<ProjectVO.Project> queryProjectPage(ProjectParam.ProjectPageQuery pageQuery);

    void addProject(ProjectParam.AddProject addProject);

    void updateProject(ProjectParam.UpdateProject updateProject);

    void deleteProjectById(int id);

    DataTable<ProjectVO.TenantDetail> queryProjectTenantPage(ProjectParam.ProjectTenantPageQuery pageQuery);

    void addProjectTenant(ProjectParam.AddProjectTenant addProjectTenant);

    void updateProjectTenant(ProjectParam.UpdateProjectTenant updateProjectTenant);

    void deleteProjectTenantById(int id);

    void addProjectLoadBalancer(ProjectParam.AddProjectLoadBalancer param);

    void updateProjectLoadBalancer(ProjectParam.UpdateProjectLoadBalancer param);

    void deleteProjectLoadBalancerById(int id);

    List<ProjectVO.LoadBalancer> queryLoadBalancersByTenantId(int tenantId);

    void addProjectGroup(ProjectParam.AddProjectGroup param);

    List<ProjectVO.GroupDetail> queryGroupsByTenantId(int tenantId);

    void deleteProjectGroupById(int id);

}
