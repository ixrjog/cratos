package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Menu;
import com.baiyi.cratos.domain.param.menu.MenuParam;
import com.baiyi.cratos.mapper.MenuMapper;
import com.baiyi.cratos.service.MenuService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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
@BusinessType(type = BusinessTypeEnum.MENU)
public class MenuServiceImpl implements MenuService {

    private final MenuMapper menuMapper;

    @Override
    public Menu getByUniqueKey(Menu menu) {
        Example example = new Example(Menu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", menu.getName());
        return menuMapper.selectOneByExample(example);
    }

    @Override
    public DataTable<Menu> queryMenuPage(MenuParam.MenuPageQuery pageQuery) {
        Page<Menu> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<Menu> data = menuMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public List<Menu> queryMenuChildren(int menuId) {
        Example example = new Example(Menu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentId", menuId)
                .andEqualTo("menuType", "CHILDREN");
        return menuMapper.selectByExample(example);
    }

}
