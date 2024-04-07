package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.common.Converter;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Menu;
import com.baiyi.cratos.domain.view.menu.MenuVO;
import com.baiyi.cratos.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2024/4/7 下午4:04
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.MENU)
public class MenuWrapper implements Converter<Menu, MenuVO.Menu> {

    private final MenuService menuService;

    private final MenuTitleWrapper menuTitleWrapper;

    public void wrap(MenuVO.Menu menu) {
        menuTitleWrapper.wrap(menu);
        recursionWrapMenuChildren(menu);
    }

    public void recursionWrapMenuChildren(MenuVO.IMenuChildren menuChildren) {
        List<Menu> menus = menuService.queryMenuChildren(menuChildren.getMenuId());
        if (CollectionUtils.isEmpty(menus)) {
            return;
        }
        List<MenuVO.Menu> children = menus.stream()
                .map(e -> {
                    MenuVO.Menu menu = wrapToTarget(e, menuChildren.getLang());
                    recursionWrapMenuChildren(menu);
                    return menu;
                })
                .collect(Collectors.toList());
        menuChildren.setChildren(children);
    }

    public DataTable<MenuVO.Menu> wrapToTarget(DataTable<Menu> dataTable, String lang) {
        List<MenuVO.Menu> list = dataTable.getData()
                .stream()
                .map(menu -> wrapToTarget(menu, lang))
                .collect(Collectors.toList());
        return new DataTable<>(list, dataTable.getTotalNum());
    }

    public MenuVO.Menu wrapToTarget(Menu s, String lang) {
        MenuVO.Menu menu = convert(s);
        menu.setLang(lang);
        wrap(menu);
        return menu;
    }

}
