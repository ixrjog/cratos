package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.menu.MenuParam;
import com.baiyi.cratos.domain.view.menu.MenuVO;

/**
 * @Author baiyi
 * @Date 2024/4/7 下午3:38
 * @Version 1.0
 */
public interface MenuFacade {

    @Deprecated
    DataTable<MenuVO.Menu> queryMenuPage(MenuParam.MenuPageQuery pageQuery);

    MenuVO.NavMenu getNavMenu();

    MenuVO.Menu getMenuById(int id);

    void updateMenu(MenuParam.UpdateMenu updateMenu);

    void addMenu(MenuParam.AddMenu addMenu);

    void deleteMenuById(int id);

}
