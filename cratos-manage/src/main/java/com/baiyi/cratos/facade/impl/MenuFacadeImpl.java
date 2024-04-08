package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Menu;
import com.baiyi.cratos.domain.param.menu.MenuParam;
import com.baiyi.cratos.domain.view.menu.MenuVO;
import com.baiyi.cratos.facade.MenuFacade;
import com.baiyi.cratos.service.MenuService;
import com.baiyi.cratos.service.MenuTitleService;
import com.baiyi.cratos.wrapper.MenuWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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

    private final MenuWrapper menuWrapper;

    @Override
    public DataTable<MenuVO.Menu> queryMenuPage(MenuParam.MenuPageQuery pageQuery) {
        DataTable<Menu> table = menuService.queryMenuPage(pageQuery);
        return menuWrapper.wrapToTarget(table, pageQuery.getLang());
    }

}
