package com.baiyi.cratos.facade;

import com.baiyi.cratos.common.enums.AccessLevel;
import com.baiyi.cratos.domain.param.rbac.RbacUserRoleParam;
import com.baiyi.cratos.domain.view.rbac.RbacRoleVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/17 10:05
 * @Version 1.0
 */
public interface RbacFacade {

    /**
     * 验证资源访问权限
     *
     * @param token
     * @param resource
     */
    void verifyResourceAccessPermissions(String token, String resource);

    /**
     * 校验Token的访问级别
     * @param accessLevel
     * @param token
     * @return
     */
    boolean verifyRoleAccessLevel(AccessLevel accessLevel, String token);

    List<RbacRoleVO.Role> checkUserRoleResourcePermission(RbacUserRoleParam.VerifyUserRoleResourcePermission checkPermission);

}
