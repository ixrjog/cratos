package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.generator.UserPermission;
import com.baiyi.cratos.domain.view.access.AccessControlVO;
import com.baiyi.cratos.service.access.AccessControlFacade;
import com.baiyi.cratos.service.UserPermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/13 16:09
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AccessControlFacadeImpl implements AccessControlFacade {

    private final UserPermissionService userPermissionService;

    @Override
    public AccessControlVO.AccessControl generateAccessControl(BaseBusiness.HasBusiness hasBusiness, String namespace) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        String username = authentication.getName();
        if (!StringUtils.hasText(username)) {
            return AccessControlVO.AccessControl.unauthorized(hasBusiness.getBusinessType());
        }
        UserPermission uniqueKey = UserPermission.builder()
                .businessType(hasBusiness.getBusinessType())
                .businessId(hasBusiness.getBusinessId())
                .username(username)
                .role(namespace)
                .build();
        UserPermission userPermission = userPermissionService.getByUniqueKey(uniqueKey);
        if (userPermission == null) {
            return AccessControlVO.AccessControl.unauthorized(hasBusiness.getBusinessType());
        }
        return AccessControlVO.AccessControl.authorized(hasBusiness.getBusinessType());
    }

    @Override
    public AccessControlVO.HasAccessControl invoke(AccessControlVO.HasAccessControl hasAccessControl) {
        AccessControlVO.AccessControl accessControl = this.generateAccessControl(SimpleBusiness.builder()
                .businessType(hasAccessControl.getBusinessType())
                .businessId(hasAccessControl.getBusinessId())
                .build(), hasAccessControl.getNamespace());
        hasAccessControl.setAccessControl(accessControl);
        return hasAccessControl;
    }

}
