package com.baiyi.cratos.facade.project.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.param.http.project.ProjectParam;
import com.baiyi.cratos.domain.view.project.ProjectLoadBalancerVO;
import com.baiyi.cratos.domain.view.project.ProjectVO;
import com.baiyi.cratos.eds.aliyun.facade.AliyunLoadBalancerFacade;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.huaweicloud.cloud.facade.HwcLoadBalancerFacade;
import com.baiyi.cratos.facade.project.ProjectFacade;
import com.baiyi.cratos.service.*;
import com.baiyi.cratos.wrapper.project.ProjectLoadBalancerWrapper;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/5/26 11:17
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class ProjectFacadeImpl implements ProjectFacade {

    private final AliyunLoadBalancerFacade aliyunLbFacade;
    private final HwcLoadBalancerFacade hwcLbFacade;

    private final ProjectService projectService;
    private final ProjectTenantService projectTenantService;
    private final ProjectLoadBalancerService projectLoadBalancerService;
    private final ProjectGroupService projectGroupService;
    private final ProjectGroupMemberService projectGroupMemberService;
    private final EdsInstanceService edsInstanceService;
    private final ProjectLoadBalancerWrapper projectLoadBalancerWrapper;

    @Override
    public ProjectVO.TenantView queryProjectTenantView(ProjectParam.ProjectTenantViewQuery queryParam) {
        Project project = projectService.getByProjectKey(queryParam.getProjectKey());
        ProjectTenant tenant;
        if (queryParam.getTenantId() != null) {
            tenant = projectTenantService.getById(queryParam.getTenantId());
        } else {
            tenant = projectTenantService.getTenant(project.getId(), queryParam.getTenantCode());
        }
        List<ProjectLoadBalancerVO.LoadBalancer> loadBalancers = Lists.newArrayList();
        List<ProjectLoadBalancer> projectLoadBalancers = projectLoadBalancerService.queryByTenantId(tenant.getId());
        projectLoadBalancers.forEach(projectLoadBalancer -> {
            try {
                EdsInstance instance = edsInstanceService.getById(projectLoadBalancer.getInstanceId());
                EdsInstanceTypeEnum instanceType = EdsInstanceTypeEnum.valueOf(instance.getEdsType());
                if (EdsInstanceTypeEnum.ALIYUN.equals(instanceType)) {
                    ProjectLoadBalancerVO.LoadBalancer loadBalancer = aliyunLbFacade.getLoadBalancer(
                            projectLoadBalancer);
                    loadBalancers.add(loadBalancer);
                }
                if (EdsInstanceTypeEnum.HUAWEICLOUD.equals(instanceType)) {
                    ProjectLoadBalancerVO.LoadBalancer loadBalancer = hwcLbFacade.getLoadBalancer(projectLoadBalancer);
                    loadBalancers.add(loadBalancer);
                }
            } catch (Exception ignored) {
            }
        });
        return ProjectVO.TenantView.builder()
                .docs(tenant.getDocs())
                .loadBalancers(loadBalancers)
                .groups(getGroups(tenant.getId()))
                .build();
    }

    private List<ProjectVO.Group> getGroups(int tenantId) {
        return projectGroupService.queryByTenantId(tenantId)
                .stream()
                .map(group -> {
                    List<ProjectVO.GroupMember> members = projectGroupMemberService.queryByGroupId(group.getId())
                            .stream()
                            .map(member -> ProjectVO.GroupMember.builder()
                                    .groupId(group.getId())
                                    .businessType(member.getBusinessType())
                                    .businessId(member.getBusinessId())
                                    .role(member.getRole())
                                    .name(member.getName())
                                    .valid(member.getValid())
                                    .comment(member.getComment())
                                    .build())
                            .toList();
                    return ProjectVO.Group.builder()
                            .name(group.getName())
                            .projectId(group.getId())
                            .tenantId(group.getTenantId())
                            .valid(group.getValid())
                            .comment(group.getComment())
                            .members(members)
                            .build();
                })
                .toList();
    }

    @Override
    public List<ProjectVO.Tenant> queryProjectTenants(String projectKey) {
        Project project = projectService.getByProjectKey(projectKey);
        return projectTenantService.queryTenants(project.getId(), null, null)
                .stream()
                .map(t -> ProjectVO.Tenant.builder()
                        .id(t.getId())
                        .tenantCode(t.getTenantCode())
                        .countryCode(t.getCountryCode())
                        .name(t.getName())
                        .docs(t.getDocs())
                        .build())
                .toList();
    }

    @Override
    public DataTable<ProjectVO.Project> queryProjectPage(ProjectParam.ProjectPageQuery pageQuery) {
        DataTable<Project> table = projectService.queryProjectPage(pageQuery.toParam());
        List<ProjectVO.Project> data = table.getData()
                .stream()
                .map(e -> ProjectVO.Project.builder()
                        .id(e.getId())
                        .key(e.getKey())
                        .name(e.getName())
                        .valid(e.getValid())
                        .comment(e.getComment())
                        .createTime(e.getCreateTime())
                        .updateTime(e.getUpdateTime())
                        .build())
                .toList();
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public void addProject(ProjectParam.AddProject addProject) {
        Project project = addProject.toTarget();
        projectService.add(project);
    }

    @Override
    public void updateProject(ProjectParam.UpdateProject updateProject) {
        Project project = projectService.getById(updateProject.getId());
        if (project == null) {
            return;
        }
        project.setKey(updateProject.getKey());
        project.setName(updateProject.getName());
        project.setValid(updateProject.getValid());
        project.setComment(updateProject.getComment());
        projectService.updateByPrimaryKey(project);
    }

    @Override
    public void deleteProjectById(int id) {
        projectService.deleteById(id);
    }

    @Override
    public DataTable<ProjectVO.TenantDetail> queryProjectTenantPage(ProjectParam.ProjectTenantPageQuery pageQuery) {
        DataTable<ProjectTenant> table = projectTenantService.queryProjectTenantPage(pageQuery.toParam());
        List<ProjectVO.TenantDetail> data = table.getData()
                .stream()
                .map(e -> {
                    Project p = projectService.getById(e.getProjectId());
                    ProjectVO.Project projectVO = p != null ? ProjectVO.Project.builder()
                            .id(p.getId())
                            .key(p.getKey())
                            .name(p.getName())
                            .valid(p.getValid())
                            .comment(p.getComment())
                            .createTime(p.getCreateTime())
                            .updateTime(p.getUpdateTime())
                            .build() : null;
                    List<ProjectVO.LoadBalancer> lbs = projectLoadBalancerService.queryByTenantId(e.getId())
                            .stream()
                            .map(lb -> ProjectVO.LoadBalancer.builder()
                                    .id(lb.getId())
                                    .projectId(lb.getProjectId())
                                    .tenantId(lb.getTenantId())
                                    .assetId(lb.getAssetId())
                                    .instanceId(lb.getInstanceId())
                                    .name(lb.getName())
                                    .valid(lb.getValid())
                                    .comment(lb.getComment())
                                    .config(lb.getConfig())
                                    .build())
                            .toList();
                    return ProjectVO.TenantDetail.builder()
                            .id(e.getId())
                            .projectId(e.getProjectId())
                            .project(projectVO)
                            .tenantCode(e.getTenantCode())
                            .countryCode(e.getCountryCode())
                            .name(e.getName())
                            .docs(e.getDocs())
                            .valid(e.getValid())
                            .comment(e.getComment())
                            .createTime(e.getCreateTime())
                            .updateTime(e.getUpdateTime())
                            .loadBalancers(lbs)
                            .build();
                })
                .toList();
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public void addProjectTenant(ProjectParam.AddProjectTenant addProjectTenant) {
        ProjectTenant tenant = addProjectTenant.toTarget();
        projectTenantService.add(tenant);
    }

    @Override
    public void updateProjectTenant(ProjectParam.UpdateProjectTenant updateProjectTenant) {
        ProjectTenant tenant = projectTenantService.getById(updateProjectTenant.getId());
        if (tenant == null) {
            return;
        }
        tenant.setProjectId(updateProjectTenant.getProjectId());
        tenant.setTenantCode(updateProjectTenant.getTenantCode());
        tenant.setCountryCode(updateProjectTenant.getCountryCode());
        tenant.setName(updateProjectTenant.getName());
        tenant.setDocs(updateProjectTenant.getDocs());
        tenant.setValid(updateProjectTenant.getValid());
        tenant.setComment(updateProjectTenant.getComment());
        projectTenantService.updateByPrimaryKey(tenant);
    }

    @Override
    public void deleteProjectTenantById(int id) {
        projectTenantService.deleteById(id);
    }

    @Override
    public void addProjectLoadBalancer(ProjectParam.AddProjectLoadBalancer param) {
        ProjectLoadBalancer lb = param.toTarget();
        projectLoadBalancerService.add(lb);
    }

    @Override
    public void updateProjectLoadBalancer(ProjectParam.UpdateProjectLoadBalancer param) {
        ProjectLoadBalancer lb = projectLoadBalancerService.getById(param.getId());
        if (lb == null) return;
        lb.setProjectId(param.getProjectId());
        lb.setTenantId(param.getTenantId());
        lb.setAssetId(param.getAssetId());
        lb.setInstanceId(param.getInstanceId());
        lb.setName(param.getName());
        lb.setValid(param.getValid());
        lb.setComment(param.getComment());
        lb.setConfig(param.getConfig());
        projectLoadBalancerService.updateByPrimaryKey(lb);
    }

    @Override
    public void deleteProjectLoadBalancerById(int id) {
        projectLoadBalancerService.deleteById(id);
    }

    @Override
    public List<ProjectVO.LoadBalancer> queryLoadBalancersByTenantId(int tenantId) {
        return projectLoadBalancerService.queryByTenantId(tenantId)
                .stream()
                .map(projectLoadBalancerWrapper::wrapToTarget)
                .toList();
    }

    @Override
    public List<ProjectVO.GroupDetail> queryGroupsByTenantId(int tenantId) {
        return projectGroupService.queryByTenantId(tenantId).stream()
                .map(group -> {
                    List<ProjectVO.GroupMemberDetail> members = projectGroupMemberService.queryByGroupId(group.getId())
                            .stream()
                            .map(m -> ProjectVO.GroupMemberDetail.builder()
                                    .id(m.getId()).groupId(m.getGroupId())
                                    .businessType(m.getBusinessType()).businessId(m.getBusinessId())
                                    .role(m.getRole()).name(m.getName()).valid(m.getValid()).comment(m.getComment())
                                    .createTime(m.getCreateTime()).updateTime(m.getUpdateTime())
                                    .build())
                            .toList();
                    return ProjectVO.GroupDetail.builder()
                            .id(group.getId()).projectId(group.getProjectId()).tenantId(group.getTenantId())
                            .name(group.getName()).valid(group.getValid()).comment(group.getComment())
                            .createTime(group.getCreateTime()).updateTime(group.getUpdateTime())
                            .members(members)
                            .build();
                })
                .toList();
    }

    @Override
    public void addProjectGroup(ProjectParam.AddProjectGroup param) {
        ProjectGroup group = param.toTarget();
        projectGroupService.add(group);
    }

    @Override
    public void updateProjectGroup(ProjectParam.UpdateProjectGroup param) {
        ProjectGroup group = projectGroupService.getById(param.getId());
        if (group == null) return;
        group.setName(param.getName());
        group.setValid(param.getValid());
        group.setComment(param.getComment());
        projectGroupService.updateByPrimaryKey(group);
    }

    @Override
    public void deleteProjectGroupById(int id) {
        projectGroupService.deleteById(id);
    }

    @Override
    public void addProjectGroupMember(ProjectParam.AddProjectGroupMember param) {
        ProjectGroupMember member = param.toTarget();
        projectGroupMemberService.add(member);
    }

    @Override
    public void deleteProjectGroupMemberById(int id) {
        projectGroupMemberService.deleteById(id);
    }

}
