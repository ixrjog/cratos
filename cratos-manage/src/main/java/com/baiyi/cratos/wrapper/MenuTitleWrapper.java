package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.domain.generator.MenuTitle;
import com.baiyi.cratos.domain.view.menu.MenuVO;
import com.baiyi.cratos.domain.view.menu.MyMenuVO;
import com.baiyi.cratos.service.MenuTitleService;
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
 * @Date 2024/4/7 下午5:04
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MenuTitleWrapper extends BaseDataTableConverter<MenuVO.Title, MenuTitle> implements IBaseWrapper<MenuVO.Title> {

    private final MenuTitleService menuTitleService;

    @Override
    public void wrap(MenuVO.Title title) {
    }

    public void wrap(MenuVO.Menu menu) {
        IdentityUtil.validIdentityRun(menu.getMenuId())
                .withTrue(() -> menu.setMenuTitles(menuTitleService.queryByMenuId(menu.getMenuId())
                        .stream()
                        .map(this::wrapToTarget)
                        .collect(Collectors.toList())));
    }

    public void wrap(MyMenuVO.MyMenu menu) {
        MenuTitle uniqueKey = MenuTitle.builder()
                .menuId(menu.getMenuId())
                .lang(menu.getLang())
                .build();

        MenuTitle menuTitle = menuTitleService.getByUniqueKey(uniqueKey);
        if (menuTitle != null) {
            menu.setTitle(menuTitle.getTitle());
        } else {
            List<MenuTitle> menuTitles = menuTitleService.queryByMenuId(menu.getMenuId());
            if (CollectionUtils.isEmpty(menuTitles)) {
                // 未配置 Title 则降级成 Name
                menu.setTitle(menu.getName());
            }
            MenuTitle preferenceMenuTitle = menuTitles.stream()
                    .filter(MenuTitle::getPreference)
                    .findFirst()
                    .orElse(menuTitles.get(0));
            menu.setTitle(preferenceMenuTitle.getTitle());
        }
    }

}
