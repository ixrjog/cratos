package com.baiyi.cratos.common.merger;

import com.baiyi.cratos.domain.generator.UserPermission;
import com.baiyi.cratos.domain.view.user.UserPermissionVO;
import com.google.common.collect.Maps;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/11 10:44
 * &#064;Version 1.0
 */
public class UserPermissionMerger {

    private List<UserPermission> userPermissions;

    private Map<String, Map<Integer, UserPermissionVO.MergedPermissions>> permissions;

    public static UserPermissionMerger newMerger() {
        UserPermissionMerger merger = new UserPermissionMerger();
        merger.permissions = Maps.newHashMap();
        return merger;
    }

    public UserPermissionMerger withUserPermissions(List<UserPermission> userPermissions) {
        this.userPermissions = userPermissions;
        return this;
    }

    private UserPermissionMerger merge() {
        if (CollectionUtils.isEmpty(this.userPermissions)) {
            return this;
        }
        this.userPermissions.stream()
                .collect(Collectors.groupingBy(UserPermission::getBusinessType))
                .forEach((businessType, v) -> {
                    Map<Integer, UserPermissionVO.MergedPermissions> mergedPermissionsMap = merge(businessType, v);
                    this.permissions.put(businessType, mergedPermissionsMap);
                });
        return this;
    }

    private Map<Integer, UserPermissionVO.MergedPermissions> merge(String businessType, List<UserPermission> v) {
        Map<Integer, UserPermissionVO.MergedPermissions> mergedPermissionsMap = Maps.newHashMap();
        v.stream()
                .collect(Collectors.groupingBy(UserPermission::getBusinessId))
                .forEach((businessId, permissions) -> permissions.forEach(
                        permission -> merge(permission, mergedPermissionsMap)));
        return mergedPermissionsMap;
    }

    private void merge(UserPermission userPermission,
                       Map<Integer, UserPermissionVO.MergedPermissions> mergedPermissionsMap) {
        final Integer businessId = userPermission.getBusinessId();
        final String businessType = userPermission.getBusinessType();
        if (mergedPermissionsMap.containsKey(businessId)) {
            mergedPermissionsMap.get(businessId)
                    .getRoles()
                    .add(toRole(userPermission));
        } else {
            UserPermissionVO.MergedPermissions mergedPermissions = UserPermissionVO.MergedPermissions.builder()
                    .businessType(businessType)
                    .businessId(businessId)
                    .name(userPermission.getName())
                    .build();
            mergedPermissions.getRoles()
                    .add(toRole(userPermission));
            mergedPermissionsMap.put(businessId, mergedPermissions);
        }
    }

    public Map<String, List<UserPermissionVO.MergedPermissions>> get() {
        this.merge();
        Map<String, List<UserPermissionVO.MergedPermissions>> result = Maps.newHashMap();
        this.permissions.forEach((k, v) -> result.put(k, v.values()
                .stream()
                .toList()));
        return result;
    }

    private UserPermissionVO.PermissionRole toRole(UserPermission userPermission) {
        return UserPermissionVO.PermissionRole.builder()
                .role(userPermission.getRole())
                .seq(userPermission.getSeq())
                .valid(userPermission.getValid())
                .comment(userPermission.getComment())
                .expiredTime(userPermission.getExpiredTime())
                .build();
    }

}
