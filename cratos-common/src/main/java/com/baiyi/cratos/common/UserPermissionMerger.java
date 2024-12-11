package com.baiyi.cratos.common;

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
        if (CollectionUtils.isEmpty(userPermissions)) {
            return this;
        }
        Map<String, List<UserPermission>> businessPermissions = userPermissions.stream()
                .collect(Collectors.groupingBy(UserPermission::getBusinessType));
        businessPermissions.forEach((businessType, v) -> {
            final Map<Integer, UserPermissionVO.MergedPermissions> mergedPermissionsMap = Maps.newHashMap();
            v.stream()
                    .collect(Collectors.groupingBy(UserPermission::getBusinessId))
                    .forEach((businessId, permissions) -> permissions.forEach(permission -> {
                        if (mergedPermissionsMap.containsKey(businessId)) {
                            mergedPermissionsMap.get(businessId)
                                    .getRoles()
                                    .add(toRole(permission));
                        } else {
                            UserPermissionVO.MergedPermissions mergedPermissions = UserPermissionVO.MergedPermissions.builder()
                                    .businessType(businessType)
                                    .businessId(businessId)
                                    .build();
                            mergedPermissions.getRoles()
                                    .add(toRole(permission));
                            mergedPermissionsMap.put(businessId, mergedPermissions);
                        }
                    }));
            permissions.put(businessType, mergedPermissionsMap);
        });
        return this;
    }

    public Map<String, Map<Integer, UserPermissionVO.MergedPermissions>> get() {
        this.merge();
        return permissions;
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
