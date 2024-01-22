package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.constants.AccessLevel;
import com.baiyi.cratos.common.exception.auth.AuthenticationException;
import com.baiyi.cratos.common.exception.auth.AuthorizationException;
import com.baiyi.cratos.domain.ErrorEnum;
import com.baiyi.cratos.domain.generator.RbacResource;
import com.baiyi.cratos.domain.generator.UserToken;
import com.baiyi.cratos.facade.RbacFacade;
import com.baiyi.cratos.facade.UserTokenFacade;
import com.baiyi.cratos.facade.rbac.RbacResourceFacade;
import com.baiyi.cratos.facade.rbac.RbacRoleFacade;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @Author baiyi
 * @Date 2024/1/17 10:06
 * @Version 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class RbacFacadeImpl implements RbacFacade {

    private final RbacResourceFacade rbacResourceFacade;
    private final UserTokenFacade userTokenFacade;

    private final RbacRoleFacade rbacRoleFacade;

    @Override
    public void verifyResourceAccessPermissions(String token, String resource) {
        if (!StringUtils.hasText(token)) {
            throw new AuthenticationException(ErrorEnum.AUTHENTICATION_INVALID_TOKEN);
        }
        RbacResource rbacResource = rbacResourceFacade.getByResource(resource);
        if (rbacResource == null) {
            throw new AuthorizationException(ErrorEnum.AUTHENTICATION_RESOURCE_NOT_EXIST);
        }
        if (!rbacResource.getValid()) {
            // 登录用户即可访问
            return;
        }
        // 校验用户是否可以访问资源路径
        if (!userTokenFacade.verifyResourceAuthorizedToToken(token, resource)) {
            // TODO 管理员跳过验证
            if (!verifyRoleAccessLevel(AccessLevel.ADMIN, token)) {
                throw new AuthorizationException(ErrorEnum.AUTHORIZATION_FAILURE);
            }
        }
    }

    public boolean verifyRoleAccessLevel(AccessLevel accessLevel, String token) {
        UserToken userToken = userTokenFacade.verifyToken(token);
        // TODO
        return false;

    }

}
