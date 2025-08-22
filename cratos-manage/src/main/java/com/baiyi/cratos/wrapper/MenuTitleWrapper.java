package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.domain.generator.MenuTitle;
import com.baiyi.cratos.domain.view.menu.MenuVO;
import com.baiyi.cratos.domain.view.menu.MyMenuVO;
import com.baiyi.cratos.domain.view.menu.RoleMenuVO;
import com.baiyi.cratos.service.MenuTitleService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2024/4/7 下午5:04
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MenuTitleWrapper extends BaseDataTableConverter<MenuVO.Title, MenuTitle> implements IBaseWrapper<MenuVO.Title> {

    private final MenuTitleService menuTitleService;

    @Override
    public void wrap(MenuVO.Title vo) {
    }

    public void wrap(MenuVO.Menu menu) {
        IdentityUtils.validIdentityRun(menu.getMenuId())
                .withTrue(() -> menu.setMenuTitles(menuTitleService.queryByMenuId(menu.getMenuId())
                        .stream()
                        .map(this::wrapToTarget)
                        .collect(Collectors.toList())));
    }

    public void wrap(RoleMenuVO.Menu roleMenu) {
        String title = getTitle(roleMenu.getId(), roleMenu.getLang());
        if (StringUtils.hasText(title)) {
            roleMenu.setTitle(title);
        } else {
            roleMenu.setTitle(roleMenu.getName());
        }
    }

    public void wrap(MyMenuVO.MyMenu menu) {
        String title = getTitle(menu.getMenuId(), menu.getLang());
        if (StringUtils.hasText(title)) {
            menu.setTitle(title);
        } else {
            menu.setTitle(menu.getName());
        }
    }

    public String getTitle(int menuId, String lang) {
        MenuTitle uniqueKey = MenuTitle.builder()
                .menuId(menuId)
                .lang(lang)
                .build();

        MenuTitle menuTitle = menuTitleService.getByUniqueKey(uniqueKey);
        if (menuTitle != null) {
            return menuTitle.getTitle();
        } else {
            List<MenuTitle> menuTitles = menuTitleService.queryByMenuId(menuId);
            if (CollectionUtils.isEmpty(menuTitles)) {
                // 未配置 Title 则降级成 Name
                return null;
            }
            MenuTitle preferenceMenuTitle = menuTitles.stream()
                    .filter(MenuTitle::getPreference)
                    .findFirst()
                    .orElse(menuTitles.getFirst());
            return preferenceMenuTitle.getTitle();
        }
    }

}
