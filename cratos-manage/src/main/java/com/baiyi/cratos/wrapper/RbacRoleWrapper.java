package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.RbacRole;
import com.baiyi.cratos.domain.view.rbac.RbacRoleVO;
import com.baiyi.cratos.service.RbacRoleResourceService;
import com.baiyi.cratos.service.RbacRoleService;
import com.baiyi.cratos.service.RbacUserRoleService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBusinessWrapper;
import com.baiyi.cratos.wrapper.builder.ResourceCountBuilder;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.baiyi.cratos.domain.enums.BusinessTypeEnum.RBAC_RESOURCE;
import static com.baiyi.cratos.domain.enums.BusinessTypeEnum.USER;

/**
 * @Author baiyi
 * @Date 2024/1/25 13:40
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.RBAC_ROLE)
public class RbacRoleWrapper extends BaseDataTableConverter<RbacRoleVO.Role, RbacRole> implements IBusinessWrapper<RbacRoleVO.IRbacRoles, RbacRoleVO.Role> {

    private final RbacRoleService rbacRoleService;

    private final RbacRoleResourceService rbacRoleResourceService;

    private final RbacUserRoleService rbacUserRoleService;

    @Override
    public void wrap(RbacRoleVO.Role role) {
        Map<String, Integer> resourceCount = ResourceCountBuilder.newBuilder()
                .put(buildResourceCountForRbacResource(role))
                .put(buildResourceCountForUser(role))
                .build();
        role.setResourceCount(resourceCount);
    }

    private Map<String, Integer> buildResourceCountForRbacResource(RbacRoleVO.Role role) {
        Map<String, Integer> resourceCount = Maps.newHashMap();
        resourceCount.put(RBAC_RESOURCE.name(), rbacRoleResourceService.selectCountByRoleId(role.getId()));
        return resourceCount;
    }

    private Map<String, Integer> buildResourceCountForUser(RbacRoleVO.Role role) {
        Map<String, Integer> resourceCount = Maps.newHashMap();
        resourceCount.put(USER.name(), rbacUserRoleService.selectCountByRoleId(role.getId()));
        return resourceCount;
    }

    @Override
    public void businessWrap(RbacRoleVO.IRbacRoles rbacRoles) {
        if (StringUtils.isEmpty(rbacRoles.getUsername())) {
            return;
        }
        List<RbacRoleVO.Role> roles = rbacUserRoleService.queryByUsername(rbacRoles.getUsername())
                .stream()
                .map(e -> {
                    RbacRole role = rbacRoleService.getById(e.getRoleId());
                    RbacRoleVO.Role roleVO = this.convert(role);
                    // AOP增强
                    wrapFromProxy(roleVO);
                    return roleVO;
                })
                .toList();
        rbacRoles.setRbacRoles(roles);
    }

}