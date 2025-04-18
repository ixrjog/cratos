package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.domain.generator.Menu;
import com.baiyi.cratos.domain.generator.RbacRoleMenu;
import com.baiyi.cratos.domain.view.menu.RoleMenuVO;
import com.baiyi.cratos.service.MenuService;
import com.baiyi.cratos.service.RbacRoleMenuService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2024/4/12 下午1:54
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RoleMenuWrapper extends BaseDataTableConverter<RoleMenuVO.Menu, Menu> implements IBaseWrapper<RoleMenuVO.Menu> {

    private final MenuTitleWrapper menuTitleWrapper;
    private final MenuService menuService;
    private final RbacRoleMenuService roleMenuService;

    @Override
    public void wrap(RoleMenuVO.Menu vo) {
        RbacRoleMenu uniqueKey = RbacRoleMenu.builder()
                .roleId(vo.getRoleId())
                .menuId(vo.getId())
                .build();
        vo.setIsChecked(roleMenuService.getByUniqueKey(uniqueKey) != null);
        vo.setTitle(vo.getName());
        vo.setDisabled(!vo.getValid());
        vo.setOpen(true);
        menuTitleWrapper.wrap(vo);
        recursionWrapRoleMenuChildren(vo);
    }

    public void recursionWrapRoleMenuChildren(RoleMenuVO.Menu roleMenu) {
        List<Menu> menus = menuService.querySubMenu(roleMenu.getId());
        if (CollectionUtils.isEmpty(menus)) {
            return;
        }
        List<RoleMenuVO.Menu> children = menus.stream()
                .map(e -> {
                    RoleMenuVO.Menu menu = wrapToTarget(e, roleMenu.getLang(), roleMenu.getRoleId());
                    recursionWrapRoleMenuChildren(menu);
                    return menu;
                })
                .sorted(Comparator.comparingInt(RoleMenuVO.Menu::getSeq))
                .collect(Collectors.toList());
        roleMenu.setItems(children);
    }

    public RoleMenuVO.Menu wrapToTarget(Menu s, String lang, int roleId) {
        RoleMenuVO.Menu menu = convert(s);
        menu.setLang(lang);
        menu.setRoleId(roleId);
        wrap(menu);
        return menu;
    }

}