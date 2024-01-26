package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.domain.generator.RbacRole;
import com.baiyi.cratos.domain.view.rbac.RbacRoleVO;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/25 13:40
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RbacRoleWrapper extends BaseDataTableConverter<RbacRoleVO.Role, RbacRole> implements IBaseWrapper<RbacRoleVO.Role> {

    @Override
    //@BusinessWrapper(types = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC})
    public void wrap(RbacRoleVO.Role role) {
        // This is a good idea
    }

    public void wrap(RbacRoleVO.IRbacRoles target) {
        if (StringUtils.isEmpty(target.getUsername())) {
            return;
        }
//        List<AuthUserRole> roles = authUserRoleService.queryByUsername(iRoles.getUsername());
//        iRoles.setRoles(roles.stream().map(e ->
//                BeanCopierUtil.copyProperties(authRoleService.getById(e.getRoleId()), AuthRoleVO.Role.class)
//        ).collect(Collectors.toList()));
    }

}