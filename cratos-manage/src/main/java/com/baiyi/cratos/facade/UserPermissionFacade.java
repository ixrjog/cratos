package com.baiyi.cratos.facade;

import com.baiyi.cratos.common.constants.PermissionRoleEnum;
import com.baiyi.cratos.domain.BaseBusiness;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2024/1/18 18:18
 * @Version 1.0
 */
public interface UserPermissionFacade {

    boolean verify(String username, BaseBusiness.IBusiness business, PermissionRoleEnum permissionRoleEnum);

    void revokeUserBusinessPermission(String username, BaseBusiness.IBusiness business);

    void grantUserBusinessPermission(String username, BaseBusiness.IBusiness business, PermissionRoleEnum permissionRoleEnum, Date expirationTime);

    default void grantUserBusinessPermission(String username, BaseBusiness.IBusiness business, Date expirationTime) {
        grantUserBusinessPermission(username, business, PermissionRoleEnum.BASE, expirationTime);
    }

}
