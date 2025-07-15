package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.enums.AccessLevel;
import com.baiyi.cratos.common.util.ExpiredUtil;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.generator.UserPermission;
import com.baiyi.cratos.domain.view.access.AccessControlVO;
import com.baiyi.cratos.facade.AccessControlFacade;
import com.baiyi.cratos.facade.RbacRoleFacade;
import com.baiyi.cratos.service.ApplicationService;
import com.baiyi.cratos.service.UserPermissionService;
import com.baiyi.cratos.workorder.holder.ApplicationDeletePodTokenHolder;
import com.baiyi.cratos.workorder.holder.ApplicationRedeployTokenHolder;
import com.baiyi.cratos.workorder.holder.token.ApplicationDeletePodToken;
import com.baiyi.cratos.workorder.holder.token.ApplicationRedeployToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

import static com.baiyi.cratos.domain.view.access.AccessControlVO.OperationPermission.DEPLOYMENT_POD_DELETE;
import static com.baiyi.cratos.domain.view.access.AccessControlVO.OperationPermission.DEPLOYMENT_REDEPLOY;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/13 16:09
 * &#064;Version 1.0
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@Slf4j
@Component
@RequiredArgsConstructor
public class AccessControlFacadeImpl implements AccessControlFacade {

    private final UserPermissionService userPermissionService;
    private final RbacRoleFacade rbacRoleFacade;
    private final ApplicationDeletePodTokenHolder applicationDeletePodTokenHolder;
    private final ApplicationRedeployTokenHolder applicationRedeployTokenHolder;
    private final ApplicationService applicationService;

    @Override
    public AccessControlVO.AccessControl generateAccessControl(BaseBusiness.HasBusiness hasBusiness, String namespace) {
        String username = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getName)
                .orElse(null);
        return this.generateAccessControl(username, hasBusiness, namespace);
    }

    @Override
    public AccessControlVO.AccessControl generateAccessControl(String username, BaseBusiness.HasBusiness hasBusiness,
                                                               String namespace) {
        if (!StringUtils.hasText(username)) {
            log.error("username is empty.");
            return AccessControlVO.AccessControl.unauthorized(hasBusiness.getBusinessType());
        }
        // 跳过鉴权
        if (rbacRoleFacade.verifyRoleAccessLevelByUsername(AccessLevel.OPS, username)) {
            return AccessControlVO.AccessControl.authorized(hasBusiness.getBusinessType());
        }
        UserPermission uniqueKey = UserPermission.builder()
                .businessType(hasBusiness.getBusinessType())
                .businessId(hasBusiness.getBusinessId())
                .username(username)
                .role(namespace)
                .build();
        UserPermission userPermission = userPermissionService.getByUniqueKey(uniqueKey);
        // fixed 过期
        if (userPermission == null || ExpiredUtil.isExpired(userPermission.getExpiredTime())) {
            return AccessControlVO.AccessControl.unauthorized(hasBusiness.getBusinessType());
        }
        return AccessControlVO.AccessControl.authorized(hasBusiness.getBusinessType());
    }

    @Override
    public AccessControlVO.HasAccessControl invoke(AccessControlVO.HasAccessControl hasAccessControl) {
        AccessControlVO.AccessControl accessControl = generateAccessControl(SimpleBusiness.builder()
                .businessType(hasAccessControl.getBusinessType())
                .businessId(hasAccessControl.getBusinessId())
                .build(), hasAccessControl.getNamespace());
        if (BusinessTypeEnum.APPLICATION.name()
                .equals(hasAccessControl.getBusinessType())) {
            handleApplicationAccessControl(hasAccessControl, accessControl);
        }
        hasAccessControl.setAccessControl(accessControl);
        return hasAccessControl;
    }

    private void handleApplicationAccessControl(AccessControlVO.HasAccessControl hasAccessControl,
                                                AccessControlVO.AccessControl accessControl) {
        Application application = applicationService.getById(hasAccessControl.getBusinessId());
        if (Objects.isNull(application)) {
            return;
        }
        // delete pod
        ApplicationDeletePodToken.Token deleteToken = applicationDeletePodTokenHolder.getToken(
                SessionUtils.getUsername(), application.getName());
        if (deleteToken.getValid()) {
            accessControl.getOperationPermissions()
                    .put(DEPLOYMENT_POD_DELETE.name(), deleteToken);
        }
        // redeploy kubernetes
        ApplicationRedeployToken.Token redeployToken = applicationRedeployTokenHolder.getToken(
                SessionUtils.getUsername(), application.getName());
        if (redeployToken.getValid()) {
            accessControl.getOperationPermissions()
                    .put(DEPLOYMENT_REDEPLOY.name(), redeployToken);
        }
    }

}
