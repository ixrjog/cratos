package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.MenuTitle;
import com.baiyi.cratos.mapper.MenuTitleMapper;
import com.baiyi.cratos.service.MenuTitleService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/4/7 下午3:08
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.MENU_TITLE)
public class MenuTitleServiceImpl implements MenuTitleService {

    private final MenuTitleMapper menuTitleMapper;

    @Override
    public MenuTitle getByUniqueKey(@NonNull MenuTitle record) {
        Example example = new Example(MenuTitle.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("menuId", record.getMenuId())
                .andEqualTo("lang", record.getLang());
        return menuTitleMapper.selectOneByExample(example);
    }

    @Override
    public List<MenuTitle> queryByMenuId(int menuId) {
        Example example = new Example(MenuTitle.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("menuId", menuId);
        example.setOrderByClause("lang");
        return menuTitleMapper.selectByExample(example);
    }

}
