package com.baiyi.cratos.facade.rbac.impl;

import com.baiyi.cratos.common.enums.AccessLevel;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.param.http.rbac.RbacRoleParam;
import com.baiyi.cratos.domain.util.BeanCopierUtils;
import com.baiyi.cratos.domain.view.rbac.RbacGroupVO;
import com.baiyi.cratos.domain.view.rbac.RbacResourceVO;
import com.baiyi.cratos.domain.view.rbac.RbacRoleVO;
import com.baiyi.cratos.facade.RbacRoleFacade;
import com.baiyi.cratos.service.*;
import com.baiyi.cratos.wrapper.RbacRoleWrapper;
import com.google.api.client.util.Lists;
import com.google.api.client.util.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2024/1/17 17:22
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RbacRoleFacadeImpl implements RbacRoleFacade {

    private final RbacRoleService rbacRoleService;
    private final RbacUserRoleService rbacUserRoleService;
    private final RbacRoleWrapper rbacRoleWrapper;
    private final RbacResourceService rbacResourceService;
    private final RbacGroupService rbacGroupService;
    private final RbacRoleResourceService rbacRoleResourceService;
    private final RbacRoleMenuService rbacRoleMenuService;

    @Override
    public DataTable<RbacRoleVO.Role> queryRolePage(RbacRoleParam.RolePageQuery pageQuery) {
        DataTable<RbacRole> table = rbacRoleService.queryPageByParam(pageQuery);
        return rbacRoleWrapper.wrapToTarget(table);
    }

    @Override
    public List<RbacRole> queryUserRoles(String username) {
        return rbacUserRoleService.queryByUsername(username)
                .stream()
                .map(e -> rbacRoleService.getById(e.getRoleId()))
                .collect(Collectors.toList());
    }

    @Override
    public void updateRole(RbacRoleParam.UpdateRole updateRole) {
        RbacRole rbacRole = updateRole.toTarget();
        rbacRoleService.updateByPrimaryKey(rbacRole);
    }

    @Override
    public void addRole(RbacRoleParam.AddRole addRole) {
        RbacRole rbacRole = addRole.toTarget();
        rbacRoleService.add(rbacRole);
    }

    @Override
    public boolean verifyRoleAccessLevelByUsername(AccessLevel accessLevel, String username) {
        List<RbacRole> rbacRoles = this.queryUserRoles(username);
        if (CollectionUtils.isEmpty(rbacRoles)) {
            return false;
        }
        return rbacRoles.stream()
                .map(RbacRole::getAccessLevel)
                .max(Comparator.comparing(Integer::intValue))
                .orElse(0) >= accessLevel.getLevel();
    }

    @Override
    public boolean verifyRoleAccessLevelByUsername(AccessLevel accessLevel) {
        return verifyRoleAccessLevelByUsername(accessLevel, SessionUtils.getUsername());
    }

    @Override
    public RbacRoleVO.RoleDetails queryRoleDetails(int id) {
        RbacRole rbacRole = rbacRoleService.getById(id);
        List<Integer> resourceIds = rbacRoleResourceService.queryResourceIds(id);
        Map<Integer, RbacGroup> rbacGroupMap = Maps.newHashMap();
        Map<Integer, List<RbacResource>> groupResourcesMap = queryGroupResourcesMap(id, rbacGroupMap);
        List<RbacRoleVO.GroupResource> groupResources = groupResourcesMap.entrySet()
                .stream()
                .map(entry -> {
                    RbacGroupVO.Group group = BeanCopierUtils.copyProperties(
                            rbacGroupMap.get(entry.getKey()),
                            RbacGroupVO.Group.class
                    );
                    List<RbacResourceVO.Resource> resources = entry.getValue()
                            .stream()
                            .map(r -> BeanCopierUtils.copyProperties(r, RbacResourceVO.Resource.class))
                            .collect(Collectors.toList());
                    return RbacRoleVO.GroupResource.builder()
                            .group(group)
                            .resources(resources)
                            .build();
                })
                .collect(Collectors.toList());
        return RbacRoleVO.RoleDetails.builder()
                .role(BeanCopierUtils.copyProperties(rbacRole, RbacRoleVO.Role.class))
                .groupResources(groupResources)
                .build();
    }

    private Map<Integer, List<RbacResource>> queryGroupResourcesMap(int roleId, Map<Integer, RbacGroup> rbacGroupMap) {
        List<Integer> resourceIds = rbacRoleResourceService.queryResourceIds(roleId);
        Map<Integer, List<RbacResource>> groupResourcesMap = Maps.newHashMap();
        for (Integer resourceId : resourceIds) {
            RbacResource rbacResource = rbacResourceService.getById(resourceId);
            if (rbacResource == null) continue;
            int groupId = rbacResource.getGroupId();
            rbacGroupMap.computeIfAbsent(groupId, k -> rbacGroupService.getById(groupId));
            groupResourcesMap.computeIfAbsent(groupId, k -> Lists.newArrayList())
                    .add(rbacResource);
        }
        return groupResourcesMap;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void deleteRoleById(int id) {
        RbacRole rbacRole = rbacRoleService.getById(id);
        if (rbacRole == null) {
            return;
        }
        // 删除 rbac_role_resource
        List<RbacRoleResource> rbacRoleResources = rbacRoleResourceService.queryByRoleId(id);
        if (!CollectionUtils.isEmpty(rbacRoleResources)) {
            for (RbacRoleResource rbacRoleResource : rbacRoleResources) {
                rbacRoleResourceService.deleteById(rbacRoleResource.getId());
            }
        }
        // 删除 rbac_user_role
        List<RbacUserRole> rbacUserRoles = rbacUserRoleService.queryByRoleId(id);
        if (!CollectionUtils.isEmpty(rbacUserRoles)) {
            for (RbacUserRole rbacUserRole : rbacUserRoles) {
                rbacUserRoleService.deleteById(rbacUserRole.getId());
            }
        }
        // 删除 rbac_role_menu
        List<RbacRoleMenu> rbacRoleMenus = rbacRoleMenuService.queryByRoleId(id);
        if (!CollectionUtils.isEmpty(rbacRoleMenus)) {
            for (RbacRoleMenu rbacRoleMenu : rbacRoleMenus) {
                rbacRoleMenuService.deleteById(rbacRoleMenu.getId());
            }
        }
        rbacRoleService.deleteById(id);
    }

}
