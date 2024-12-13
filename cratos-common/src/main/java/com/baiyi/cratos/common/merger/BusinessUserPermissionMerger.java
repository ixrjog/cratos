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
        return new BusinessUserPermissionMerger();
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
        this.grouping();
        return this.details;
    }

    private void grouping() {
        //  Map<{role}, List<username>>
        Map<String, List<String>> permissionByRole = Maps.newHashMap();
        //  Map<{username}, List<role>>
        Map<String, List<String>> permissionByUsername = Maps.newHashMap();
        userPermissions.forEach(userPermission -> {
            groupingByRole(permissionByRole, userPermission);
            groupingByUsername(permissionByUsername, userPermission);
        });
        this.details = UserPermissionVO.BusinessUserPermissionDetails.builder()
                .permissionByUser(permissionByUsername)
                .permissionByRole(permissionByRole)
                .build();
    }

    private void groupingByRole(Map<String, List<String>> permissionByRole, UserPermission userPermission) {
        if (permissionByRole.containsKey(userPermission.getRole())) {
            permissionByRole.get(userPermission.getRole())
                    .add(userPermission.getUsername());
        } else {
            List<String> usernames = Lists.newArrayList();
            usernames.add(userPermission.getUsername());
            permissionByRole.put(userPermission.getRole(), usernames);
        }
    }

    private void groupingByUsername(Map<String, List<String>> permissionByUsername, UserPermission userPermission) {
        if (permissionByUsername.containsKey(userPermission.getUsername())) {
            permissionByUsername.get(userPermission.getUsername())
                    .add(userPermission.getRole());
        } else {
            List<String> roles = Lists.newArrayList();
            roles.add(userPermission.getRole());
            permissionByUsername.put(userPermission.getUsername(), roles);
        }
    }

}
