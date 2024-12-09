package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.UserPermission;
import com.baiyi.cratos.domain.param.http.user.UserPermissionParam;
import com.baiyi.cratos.domain.view.user.UserPermissionVO;
import com.baiyi.cratos.facade.UserPermissionFacade;
import com.baiyi.cratos.service.UserPermissionService;
import com.baiyi.cratos.wrapper.UserPermissionWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/18 18:18
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class UserPermissionFacadeImpl implements UserPermissionFacade {

    private final UserPermissionService userPermissionService;
    private final UserPermissionWrapper userPermissionWrapper;

    @Override
    public DataTable<UserPermissionVO.Permission> queryUserPermissionPage(
            UserPermissionParam.UserPermissionPageQuery pageQuery) {
        DataTable<UserPermission> table = userPermissionService.queryUserPermissionPage(pageQuery);
        return userPermissionWrapper.wrapToTarget(table);
    }

    @Override
    public void grantUserPermission(UserPermissionParam.GrantUserPermission grantUserPermission) {
        UserPermission userPermission = grantUserPermission.toTarget();
        if (userPermissionService.getByUniqueKey(userPermission) == null) {
            userPermissionService.add(userPermission);
        }
    }

    @Override
    public void revokeUserPermission(UserPermissionParam.RevokeUserPermission revokeUserPermission) {
        UserPermission uniqueKey = revokeUserPermission.toTarget();
        UserPermission userPermission = userPermissionService.getByUniqueKey(uniqueKey);
        if (userPermission != null) {
            this.deleteUserPermissionById(userPermission.getId());
        }
    }

    @Override
    public void deleteUserPermissionById(int id) {
        userPermissionService.deleteById(id);
    }

    @Override
    public boolean contains(String username, BaseBusiness.HasBusiness hasBusiness) {
        return userPermissionService.contains(username, hasBusiness);
    }

    @Override
    public boolean contains(String username, BaseBusiness.HasBusiness hasBusiness, String role) {
        return userPermissionService.contains(username, hasBusiness, role);
    }


//    @Override
//    public boolean verify(String username, BaseBusiness.HasBusiness business, PermissionRoleEnum permissionRoleEnum) {
//        UserPermission uniqueKey = UserPermission.builder()
//                .username(username)
//                .businessType(business.getBusinessType())
//                .businessId(business.getBusinessId())
//                .build();
//        UserPermission userPermission = userPermissionService.getByUniqueKey(uniqueKey);
//        try {
//            PermissionRoleEnum roleEnum = PermissionRoleEnum.valueOf(userPermission.getPermissionRole()
//                    .toUpperCase());
//            if (roleEnum == permissionRoleEnum) {
//                return true;
//            }
//            return roleEnum.getCode() > permissionRoleEnum.getCode();
//        } catch (IllegalArgumentException e) {
//            // 枚举类转换错误
//            return false;
//        }
//    }

    /**
     * Revoke user's business object permissions
     *
     * @param username
     * @param business
     */
//    @Override
//    public void revokeUserBusinessPermission(String username, BaseBusiness.HasBusiness business) {
//        UserPermission uniqueKey = UserPermission.builder()
//                .username(username)
//                .businessType(business.getBusinessType())
//                .businessId(business.getBusinessId())
//                .build();
//        UserPermission userPermission = userPermissionService.getByUniqueKey(uniqueKey);
//        if (userPermission != null) {
//            userPermissionService.deleteById(userPermission.getId());
//        }
//    }
//
//    @Override
//    public void grantUserBusinessPermission(String username, BaseBusiness.HasBusiness business, PermissionRoleEnum permissionRoleEnum, Date expirationTime) {
//        UserPermission uniqueKey = UserPermission.builder()
//                .username(username)
//                .businessType(business.getBusinessType())
//                .businessId(business.getBusinessId())
//                .build();
//        UserPermission userPermission = userPermissionService.getByUniqueKey(uniqueKey);
//        if (userPermission != null) {
//            UserPermission updateUserPermission = UserPermission.builder()
//                    .id(userPermission.getId())
//                    .permissionRole(permissionRoleEnum.name())
//                    .valid(true)
//                    .expiredTime(expirationTime)
//                    .build();
//            userPermissionService.updateByPrimaryKeySelective(updateUserPermission);
//        } else {
//            UserPermission addUserPermission = UserPermission.builder()
//                    .username(username)
//                    .businessType(business.getBusinessType())
//                    .businessId(business.getBusinessId())
//                    .permissionRole(permissionRoleEnum.name())
//                    .valid(true)
//                    .expiredTime(expirationTime)
//                    .rate(0)
//                    .build();
//            userPermissionService.add(addUserPermission);
//        }
//    }

}
