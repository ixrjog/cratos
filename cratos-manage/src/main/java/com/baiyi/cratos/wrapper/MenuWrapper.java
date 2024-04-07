package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.common.Converter;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Menu;
import com.baiyi.cratos.domain.view.menu.MenuVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

    private final MenuTitleWrapper menuTitleWrapper;

    public void wrap(MenuVO.Menu menu) {
        menuTitleWrapper.wrap(menu);
    }

    public DataTable<MenuVO.Menu> wrapToTarget(DataTable<Menu> dataTable, String lang) {
        List<MenuVO.Menu> list = new ArrayList<>();
        for (Menu menu : dataTable.getData()) {
            MenuVO.Menu wrapToTarget = wrapToTarget(menu, lang);
            list.add(wrapToTarget);
        }
        return new DataTable<>(list, dataTable.getTotalNum());
    }

    public MenuVO.Menu wrapToTarget(Menu s, String lang) {
        MenuVO.Menu menu = convert(s);
        menu.setLang(lang);
        wrap(menu);
        return menu;
    }

}
