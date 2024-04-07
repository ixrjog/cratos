package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Menu;
import com.baiyi.cratos.domain.param.menu.MenuParam;
import com.baiyi.cratos.mapper.MenuMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;

/**
 * @Author baiyi
 * @Date 2024/4/7 下午3:07
 * @Version 1.0
 */
public interface MenuService extends BaseUniqueKeyService<Menu>, BaseValidService<Menu, MenuMapper> {

    DataTable<Menu> queryMenuPage(MenuParam.MenuPageQuery pageQuery);

}
