package com.baiyi.cratos.facade;

import com.baiyi.cratos.common.enums.AccessLevel;
import com.baiyi.cratos.domain.generator.Robot;
import com.baiyi.cratos.domain.generator.UserToken;
import com.baiyi.cratos.domain.param.http.rbac.RbacUserRoleParam;
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
    @Deprecated
    void verifyResourceAccessPermissions(String token, String resource);


    void verifyResourceAccessPermissions(UserToken userToken, String resource);

    void verifyResourceAccessPermissions(Robot robot, String resource);

    /**
     * 校验Token的访问级别
     * @param accessLevel
     * @param token
     * @return
     */
    boolean verifyRoleAccessLevel(AccessLevel accessLevel, String token);

    void verifyResourceAccessPermissionsForUsername(String username, String resource);

    List<RbacRoleVO.Role> checkUserRoleResourcePermission(RbacUserRoleParam.VerifyUserRoleResourcePermission checkPermission);

}
