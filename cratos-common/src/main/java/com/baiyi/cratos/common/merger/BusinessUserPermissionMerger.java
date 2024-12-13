package com.baiyi.cratos.common.merger;

import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.generator.UserPermission;
import com.baiyi.cratos.domain.view.user.UserPermissionVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/12 18:19
 * &#064;Version 1.0
 */
public class BusinessUserPermissionMerger {

    private List<UserPermission> userPermissions;

    private Map<String, User> users;

    private UserPermissionVO.BusinessUserPermissionDetails details;

    public static BusinessUserPermissionMerger newMerger() {
        BusinessUserPermissionMerger merger = new BusinessUserPermissionMerger();
        merger.details = UserPermissionVO.BusinessUserPermissionDetails.builder()
                .build();
        return merger;
    }

    public BusinessUserPermissionMerger withUserPermissions(List<UserPermission> userPermissions) {
        this.userPermissions = userPermissions;
        return this;
    }

    public BusinessUserPermissionMerger withUsers(Map<String, User> users) {
        this.users = users;
        return this;
    }

    public UserPermissionVO.BusinessUserPermissionDetails get() {
        this.merge();
        return this.details;
    }

    private void merge() {
        groupingByRole();
        groupingByUsername();
    }

    private void groupingByRole() {
        //  Map<{role}, List<username>>
        Map<String, List<String>> permissionByRole = Maps.newHashMap();
        for (UserPermission userPermission : userPermissions) {
            if (permissionByRole.containsKey(userPermission.getRole())) {
                permissionByRole.get(userPermission.getRole())
                        .add(userPermission.getUsername());
            } else {
                List<String> usernames = Lists.newArrayList();
                usernames.add(userPermission.getUsername());
                permissionByRole.put(userPermission.getRole(), usernames);
            }
        }
        details.setPermissionByRole(permissionByRole);
    }

    private void groupingByUsername() {
        //  Map<{username}, List<role>>
        Map<String, List<String>> permissionByUsername = Maps.newHashMap();
        for (UserPermission userPermission : userPermissions) {
            if (permissionByUsername.containsKey(userPermission.getUsername())) {
                permissionByUsername.get(userPermission.getUsername())
                        .add(userPermission.getRole());
            } else {
                List<String> roles = Lists.newArrayList();
                roles.add(userPermission.getRole());
                permissionByUsername.put(userPermission.getUsername(), roles);
            }
        }
        details.setPermissionByUser(permissionByUsername);
    }

}
