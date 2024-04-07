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

    DataTable<MenuVO.Menu> queryMenuPage(MenuParam.MenuPageQuery pageQuery);

}
