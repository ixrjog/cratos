package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.domain.generator.UserPermission;
import com.baiyi.cratos.domain.view.user.UserPermissionVO;
import com.baiyi.cratos.service.RbacUserRoleService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.BaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/6 16:09
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserPermissionWrapper extends BaseDataTableConverter<UserPermissionVO.Permission, UserPermission> implements BaseWrapper<UserPermissionVO.Permission> {

    private final RbacUserRoleService rbacUserRoleService;

    @Override
    public void wrap(UserPermissionVO.Permission vo) {
    }

}