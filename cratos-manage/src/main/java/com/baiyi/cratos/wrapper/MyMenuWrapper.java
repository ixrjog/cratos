package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.common.Converter;
import com.baiyi.cratos.domain.generator.Menu;
import com.baiyi.cratos.domain.view.menu.MyMenuVO;
import com.baiyi.cratos.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2024/4/8 下午2:13
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MyMenuWrapper implements Converter<Menu, MyMenuVO.MyMenu> {

    private final MenuService menuService;
    private final MenuTitleWrapper menuTitleWrapper;

    public void wrap(MyMenuVO.MyMenu menu, List<Integer> myMenuIds) {
        menuTitleWrapper.wrap(menu);
        recursionWrapMenuChildren(menu, myMenuIds);
    }

    public void recursionWrapMenuChildren(MyMenuVO.HasMyMenuChildren menuChildren, List<Integer> myMenuIds) {
        List<Menu> myMenus = menuService.queryMySubMenu(menuChildren.getMenuId(), myMenuIds);
        if (CollectionUtils.isEmpty(myMenus)) {
            return;
        }
        List<MyMenuVO.MyMenu> children = myMenus.stream()
                .map(e -> {
                    MyMenuVO.MyMenu menu = wrapToTarget(e, menuChildren.getLang(), myMenuIds);
                    recursionWrapMenuChildren(menu, myMenuIds);
                    return menu;
                })
                .sorted(Comparator.comparingInt(MyMenuVO.MyMenu::getSeq))
                .collect(Collectors.toList());
        menuChildren.setChildren(children);
    }

    public List<MyMenuVO.MyMenu> wrapToTarget(List<Menu> data, String lang, List<Integer> myMenuIds) {
        return data.stream()
                .map(menu -> wrapToTarget(menu, lang, myMenuIds))
                .sorted(Comparator.comparingInt(MyMenuVO.MyMenu::getSeq))
                .collect(Collectors.toList());
    }

    public MyMenuVO.MyMenu wrapToTarget(Menu s, String lang, List<Integer> myMenuIds) {
        MyMenuVO.MyMenu menu = convert(s);
        menu.setLang(lang);
        wrap(menu, myMenuIds);
        return menu;
    }

}
