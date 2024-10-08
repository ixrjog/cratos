package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.domain.generator.Menu;
import com.baiyi.cratos.domain.view.menu.RoleMenuVO;
import com.baiyi.cratos.facade.RoleMenuFacade;
import com.baiyi.cratos.service.MenuService;
import com.baiyi.cratos.service.RbacRoleMenuService;
import com.baiyi.cratos.wrapper.RoleMenuWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2024/4/12 下午1:41
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RoleMenuFacadeImpl implements RoleMenuFacade {

    private final MenuService menuService;
    private final RbacRoleMenuService roleMenuService;
    private final RoleMenuWrapper menuWrapper;

    public RoleMenuVO.RoleMenu getRoleMenu(int roleId, String lang) {
        List<Menu> menus = menuService.queryMainMenu();
        List<RoleMenuVO.Menu> items = menus.stream()
                .map(e -> menuWrapper.wrapToTarget(e, lang, roleId))
                .sorted(Comparator.comparingInt(RoleMenuVO.Menu::getSeq))
                .collect(Collectors.toList());
        return RoleMenuVO.RoleMenu.builder()
                .items(items)
                .build();
    }

}
