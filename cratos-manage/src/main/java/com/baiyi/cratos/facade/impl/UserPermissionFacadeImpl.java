package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.constants.PermissionRoleEnum;
import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.generator.UserPermission;
import com.baiyi.cratos.facade.UserPermissionFacade;
import com.baiyi.cratos.service.UserPermissionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2024/1/18 18:18
 * @Version 1.0
 */
@Component
@AllArgsConstructor
public class UserPermissionFacadeImpl implements UserPermissionFacade {

    private final UserPermissionService userPermissionService;

    private static final String ROLE_BASE = "BASE";
    private static final String ROLE_OWNER = "OWNER";

    public boolean verify(String username, BaseBusiness.IBusiness business, PermissionRoleEnum permissionRoleEnum) {
        UserPermission uniqueKey = UserPermission.builder()
                .username(username)
                .businessType(business.getBusinessType())
                .businessId(business.getBusinessId())
                .build();
        UserPermission userPermission = userPermissionService.getByUniqueKey(uniqueKey);

        try {
            PermissionRoleEnum roleEnum = PermissionRoleEnum.valueOf(userPermission.getPermissionRole()
                    .toUpperCase());
            if (roleEnum == permissionRoleEnum) {
                return true;
            }
            return roleEnum.getCode() > permissionRoleEnum.getCode();
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Revoke user's business object permissions
     *
     * @param username
     * @param business
     */
    @Override
    public void revokeUserBusinessPermission(String username, BaseBusiness.IBusiness business) {
        UserPermission uniqueKey = UserPermission.builder()
                .username(username)
                .businessType(business.getBusinessType())
                .businessId(business.getBusinessId())
                .build();
        UserPermission userPermission = userPermissionService.getByUniqueKey(uniqueKey);
        if (userPermission != null) {
            userPermissionService.deleteById(userPermission.getId());
        }
    }

    @Override
    public void grantUserBusinessPermission(String username, BaseBusiness.IBusiness business, PermissionRoleEnum permissionRoleEnum, Date expirationTime) {
        UserPermission uniqueKey = UserPermission.builder()
                .username(username)
                .businessType(business.getBusinessType())
                .businessId(business.getBusinessId())
                .build();
        UserPermission userPermission = userPermissionService.getByUniqueKey(uniqueKey);
        if (userPermission != null) {
            UserPermission updateUserPermission = UserPermission.builder()
                    .id(userPermission.getId())
                    .permissionRole(permissionRoleEnum.name())
                    .valid(true)
                    .expiredTime(expirationTime)
                    .build();
            userPermissionService.updateByPrimaryKeySelective(updateUserPermission);
        } else {
            UserPermission addUserPermission = UserPermission.builder()
                    .username(username)
                    .businessType(business.getBusinessType())
                    .businessId(business.getBusinessId())
                    .permissionRole(permissionRoleEnum.name())
                    .valid(true)
                    .expiredTime(expirationTime)
                    .rate(0)
                    .build();
            userPermissionService.add(addUserPermission);
        }
    }

}
