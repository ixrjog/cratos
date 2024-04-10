package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.exception.MenuException;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Menu;
import com.baiyi.cratos.domain.param.menu.MenuParam;
import com.baiyi.cratos.domain.view.menu.MenuVO;
import com.baiyi.cratos.facade.MenuFacade;
import com.baiyi.cratos.service.MenuService;
import com.baiyi.cratos.service.MenuTitleService;
import com.baiyi.cratos.wrapper.Menu2Wrapper;
import com.baiyi.cratos.wrapper.MenuWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2024/4/7 下午3:38
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MenuFacadeImpl implements MenuFacade {

    private final MenuService menuService;

    private final MenuTitleService menuTitleService;

    private final Menu2Wrapper menu2Wrapper;

    private final MenuWrapper menuWrapper;

    @Override
    @Deprecated
    public DataTable<MenuVO.Menu> queryMenuPage(MenuParam.MenuPageQuery pageQuery) {
        DataTable<Menu> table = menuService.queryMenuPage(pageQuery);
        return menu2Wrapper.wrapToTarget(table, pageQuery.getLang());
    }

    @Override
    public MenuVO.NavMenu getNavMenu() {
        List<Menu> menus = menuService.queryMainMenu();
        List<MenuVO.Menu> items = menus.stream()
                .map(menuWrapper::wrapToTarget)
                .collect(Collectors.toList());
        return MenuVO.NavMenu.builder()
                .items(items)
                .build();
    }

    @Override
    public MenuVO.Menu getMenuById(int id) {
        Menu menu = menuService.getById(id);
        if (menu == null) {
            return null;
        }
        return menuWrapper.wrapToTarget(menu);
    }

    @Override
    public void updateMenu(MenuParam.UpdateMenu updateMenu) {
        Menu menu = updateMenu.toTarget();
        Menu dbMenu = menuService.getById(menu.getId());
        if (dbMenu == null) {
            return;
        }
        dbMenu.setIcon(menu.getIcon());
        dbMenu.setName(menu.getName());
        dbMenu.setLink(menu.getLink());
        dbMenu.setParentId(menu.getParentId());
        dbMenu.setSeq(menu.getSeq());
        dbMenu.setValid(menu.getValid());
        menuService.updateByPrimaryKey(dbMenu);
    }

    @Override
    public void addMenu(MenuParam.AddMenu addMenu) {
        Menu menu = addMenu.toTarget();
        if (menuService.getByUniqueKey(menu) == null) {
            menuService.add(menu);
        }
    }

    @Override
    public void deleteMenuById(int id) {
        Menu menu = menuService.getById(id);
        if (menu == null) {
            return;
        }
        if (!CollectionUtils.isEmpty(menuService.querySubMenu(id))) {
            throw new MenuException("Please delete the associated sub dishes first.");
        }
        menuTitleService.queryByMenuId(id).forEach(e-> menuTitleService.deleteById(e.getId()));
        menuService.deleteById(id);
    }

}
