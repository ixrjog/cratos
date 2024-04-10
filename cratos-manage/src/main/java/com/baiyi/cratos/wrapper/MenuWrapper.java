package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Menu;
import com.baiyi.cratos.domain.view.menu.MenuVO;
import com.baiyi.cratos.service.MenuService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @Author baiyi
 * @Date 2024/4/10 下午1:52
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.MENU)
public class MenuWrapper extends BaseDataTableConverter<MenuVO.Menu, Menu> implements IBaseWrapper<MenuVO.Menu> {

    private final MenuTitleWrapper menuTitleWrapper;

    private final MenuService menuService;

    @Override
    //@BusinessWrapper(ofTypes = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC})
    public void wrap(MenuVO.Menu menu) {
        menu.setTitle(menu.getName());
        menu.setActive(false);
        menu.setOpen(true);
        menuTitleWrapper.wrap(menu);
        recursionWrapMenuChildren(menu);
    }

    /**
     * 递归
     * @param menuChildren
     */
    public void recursionWrapMenuChildren(MenuVO.IMenuChildren menuChildren) {
        List<Menu> menus = menuService.querySubMenu(menuChildren.getMenuId());
        if (CollectionUtils.isEmpty(menus)) {
            return;
        }
        List<MenuVO.Menu> children = menus.stream()
                .map(e -> {
                    MenuVO.Menu menu = wrapToTarget(e);
                    recursionWrapMenuChildren(menu);
                    return menu;
                })
                .collect(Collectors.toList());
        menuChildren.setChildren(children);
    }

}