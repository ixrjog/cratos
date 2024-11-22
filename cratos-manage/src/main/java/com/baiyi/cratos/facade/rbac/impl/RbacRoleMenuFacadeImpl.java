package com.baiyi.cratos.facade.rbac.impl;

import com.baiyi.cratos.domain.generator.RbacRoleMenu;
import com.baiyi.cratos.domain.param.http.rbac.RbacRoleMenuParam;
import com.baiyi.cratos.facade.rbac.RbacRoleMenuFacade;
import com.baiyi.cratos.service.RbacRoleMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.internal.guava.Sets;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @Author baiyi
 * @Date 2024/4/8 下午3:18
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RbacRoleMenuFacadeImpl implements RbacRoleMenuFacade {

    private final RbacRoleMenuService rbacRoleMenuService;

    @Override
    public void addRoleMenu(RbacRoleMenuParam.AddRoleMenu addRoleMenu) {
        RbacRoleMenu rbacRoleMenu = addRoleMenu.toTarget();
        if (rbacRoleMenuService.getByUniqueKey(rbacRoleMenu) == null) {
            rbacRoleMenuService.add(rbacRoleMenu);
        }
    }

    @Override
    public void deleteById(int id) {
        rbacRoleMenuService.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void saveRoleMenu(RbacRoleMenuParam.SaveRoleMenu saveRoleMenu) {
        List<RbacRoleMenu> rbacRoleMenus = rbacRoleMenuService.queryByRoleId(saveRoleMenu.getRoleId());
        if (CollectionUtils.isEmpty(rbacRoleMenus)) {
            saveRoleMenu.toRoleMenus()
                    .forEach(rbacRoleMenuService::add);
            return;
        }

        Set<Integer> menuIdSet = Sets.newHashSet();
        rbacRoleMenus.stream()
                .map(RbacRoleMenu::getMenuId)
                .forEach(menuIdSet::add);

        saveRoleMenu.toRoleMenus()
                .forEach(rbacRoleMenu -> {
                    if (!menuIdSet.contains(rbacRoleMenu.getMenuId())) {
                        rbacRoleMenuService.add(rbacRoleMenu);
                    } else {
                        menuIdSet.remove(rbacRoleMenu.getMenuId());
                    }
                });
        // 删除
        menuIdSet.stream()
                .map(i -> RbacRoleMenu.builder()
                        .menuId(i)
                        .roleId(saveRoleMenu.getRoleId())
                        .build())
                .map(rbacRoleMenuService::getByUniqueKey)
                .filter(Objects::nonNull)
                .forEach(rbacRoleMenu -> rbacRoleMenuService.deleteById(rbacRoleMenu.getId()));

    }

}
