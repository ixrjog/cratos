package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.enums.AccessLevel;
import com.baiyi.cratos.common.exception.auth.AuthenticationException;
import com.baiyi.cratos.common.exception.auth.AuthorizationException;
import com.baiyi.cratos.domain.ErrorEnum;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.param.http.rbac.RbacUserRoleParam;
import com.baiyi.cratos.domain.view.rbac.RbacRoleVO;
import com.baiyi.cratos.facade.RbacFacade;
import com.baiyi.cratos.facade.RobotFacade;
import com.baiyi.cratos.facade.UserTokenFacade;
import com.baiyi.cratos.facade.rbac.RbacResourceFacade;
import com.baiyi.cratos.facade.rbac.RbacRoleFacade;
import com.baiyi.cratos.service.RbacResourceService;
import com.baiyi.cratos.service.RbacRoleResourceService;
import com.baiyi.cratos.wrapper.RbacRoleWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.baiyi.cratos.domain.constant.Global.ROLE_FOUNDER_NAME;

/**
 * @Author baiyi
 * @Date 2024/1/17 10:06
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RbacFacadeImpl implements RbacFacade {

    private final RbacResourceFacade rbacResourceFacade;
    private final RbacResourceService rbacResourceService;
    private final UserTokenFacade userTokenFacade;
    private final RobotFacade robotFacade;
    private final RbacRoleFacade rbacRoleFacade;
    private final RbacRoleResourceService rbacRoleResourceService;
    private final RbacRoleWrapper rbacRoleWrapper;

    @Override
    public void verifyResourceAccessPermissionsForUsername(String username, String resource) {
        RbacResource rbacResource = Optional.ofNullable(rbacResourceFacade.getByResource(resource))
                .orElseThrow(() -> new AuthorizationException(ErrorEnum.AUTHENTICATION_RESOURCE_NOT_EXIST));
        if (!rbacResource.getValid()) {
            // 登录用户即可访问
            return;
        }
        // 校验用户是否可以访问资源路径
        if (rbacResourceService.countResourcesAuthorizedByUsername(username, resource) == 0) {
            // 管理员跳过验证
            if (!verifyRoleAccessLevelByUsername(AccessLevel.ADMIN, username)) {
                throw new AuthorizationException(ErrorEnum.AUTHORIZATION_FAILURE);
            }
        }
    }

    @Override
    public void verifyResourceAccessPermissions(String token, String resource) {
        if (!StringUtils.hasText(token)) {
            throw new AuthenticationException(ErrorEnum.AUTHENTICATION_INVALID_TOKEN);
        }
        RbacResource rbacResource = Optional.ofNullable(rbacResourceFacade.getByResource(resource))
                .orElseThrow(() -> new AuthorizationException(ErrorEnum.AUTHENTICATION_RESOURCE_NOT_EXIST));
        if (!rbacResource.getValid()) {
            // 登录用户即可访问
            return;
        }
        // 校验用户是否可以访问资源路径
        if (!userTokenFacade.verifyResourceAuthorizedToToken(token, resource)) {
            // 管理员跳过验证
            if (!verifyRoleAccessLevel(AccessLevel.ADMIN, token)) {
                throw new AuthorizationException(ErrorEnum.AUTHORIZATION_FAILURE);
            }
        }
    }

    @Override
    public void verifyResourceAccessPermissions(UserToken userToken, String resource) {
        if (!StringUtils.hasText(userToken.getToken())) {
            throw new AuthenticationException(ErrorEnum.AUTHENTICATION_INVALID_TOKEN);
        }
        String token = userToken.getToken();
        RbacResource rbacResource = Optional.ofNullable(rbacResourceFacade.getByResource(resource))
                .orElseThrow(() -> new AuthorizationException(ErrorEnum.AUTHENTICATION_RESOURCE_NOT_EXIST));
        if (!rbacResource.getValid()) {
            // 登录用户即可访问
            return;
        }
        // 校验用户是否可以访问资源路径
        if (!userTokenFacade.verifyResourceAuthorizedToToken(token, resource)) {
            // 管理员跳过验证
            if (!verifyRoleAccessLevel(AccessLevel.ADMIN, token)) {
                throw new AuthorizationException(ErrorEnum.AUTHORIZATION_FAILURE);
            }
        }
    }

    @Override
    public void verifyResourceAccessPermissions(Robot robot, String resource){
        if (!StringUtils.hasText(robot.getToken())) {
            throw new AuthenticationException(ErrorEnum.AUTHENTICATION_INVALID_TOKEN);
        }
        String token = robot.getToken();
        RbacResource rbacResource = Optional.ofNullable(rbacResourceFacade.getByResource(resource))
                .orElseThrow(() -> new AuthorizationException(ErrorEnum.AUTHENTICATION_RESOURCE_NOT_EXIST));
        if (!rbacResource.getValid()) {
            // 登录用户即可访问
            return;
        }
        // 校验用户是否可以访问资源路径
        if (!robotFacade.verifyResourceAuthorizedToToken(token, resource)) {
            // 管理员跳过验证
            if (!verifyRoleAccessLevel(AccessLevel.ADMIN, token)) {
                throw new AuthorizationException(ErrorEnum.AUTHORIZATION_FAILURE);
            }
        }
    }

    @Override
    public boolean verifyRoleAccessLevel(AccessLevel accessLevel, String token) {
        UserToken userToken = userTokenFacade.verifyToken(token);
        return verifyRoleAccessLevelByUsername(accessLevel, userToken.getUsername());
    }

    private boolean verifyRoleAccessLevelByUsername(AccessLevel accessLevel, String username) {
        List<RbacRole> rbacRoles = rbacRoleFacade.queryUserRoles(username);
        if (CollectionUtils.isEmpty(rbacRoles)) {
            return false;
        }
        return rbacRoles.stream()
                .map(RbacRole::getAccessLevel)
                .max(Comparator.comparing(Integer::intValue))
                .orElse(0) >= accessLevel.getLevel();
    }

    @Override
    public List<RbacRoleVO.Role> checkUserRoleResourcePermission(
            RbacUserRoleParam.VerifyUserRoleResourcePermission checkPermission) {
        return rbacRoleFacade.queryUserRoles(checkPermission.getUsername())
                .stream()
                .filter(e -> {
                    if (ROLE_FOUNDER_NAME.equals(e.getRoleName())) {
                        return true;
                    }
                    RbacRoleResource uniqueKey = RbacRoleResource.builder()
                            .roleId(e.getId())
                            .resourceId(checkPermission.getResourceId())
                            .build();
                    return rbacRoleResourceService.getByUniqueKey(uniqueKey) != null;
                })
                .map(rbacRoleWrapper::wrapToTarget)
                .toList();
    }

}
