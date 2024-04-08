package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.domain.generator.Menu;
import com.baiyi.cratos.domain.param.menu.MenuParam;
import com.baiyi.cratos.domain.view.menu.MyMenuVO;
import com.baiyi.cratos.facade.MyMenuFacade;
import com.baiyi.cratos.service.MenuService;
import com.baiyi.cratos.service.RbacRoleMenuService;
import com.baiyi.cratos.wrapper.MyMenuWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2024/4/8 上午11:21
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MyMenuFacadeImpl implements MyMenuFacade {

    private final MenuService menuService;

    private final RbacRoleMenuService rbacRoleMenuService;

    private final MyMenuWrapper myMenuWrapper;

    @Override
    public List<MyMenuVO.MyMenu> queryMyMenu(MenuParam.QueryMyMenu queryMyMenu) {
        String lang = Optional.of(queryMyMenu)
                .map(MenuParam.QueryMyMenu::getLang)
                .orElse("zh-cn");
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        if (!StringUtils.hasText(username)) {
            return MyMenuVO.MyMenu.INVALID;
        }
        return queryUserMenu(username, lang);
    }

    @Override
    public List<MyMenuVO.MyMenu> queryUserMenu(String username, String lang) {
        // 用户关联的所有菜单
        List<Integer> myMenuIds = rbacRoleMenuService.queryUserMenuIds(username);
        // 主菜单
        List<Menu> menus = menuService.queryMainMenu(myMenuIds);
        return myMenuWrapper.wrapToTarget(menus, lang, myMenuIds);
    }

}
