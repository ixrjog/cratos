package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Menu;
import com.baiyi.cratos.domain.param.http.menu.MenuParam;
import com.baiyi.cratos.mapper.MenuMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/4/7 下午3:07
 * @Version 1.0
 */
public interface MenuService extends BaseUniqueKeyService<Menu, MenuMapper>, BaseValidService<Menu, MenuMapper> {

    DataTable<Menu> queryMenuPage(MenuParam.MenuPageQuery pageQuery);

    List<Menu> querySubMenu(int parentId);

    List<Menu> queryMySubMenu(int parentId, List<Integer> myMenuIds);

    List<Menu> queryMainMenu(List<Integer> menuIds);

    List<Menu> queryMainMenu();

}
