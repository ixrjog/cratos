package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.Menu;
import com.baiyi.cratos.domain.param.menu.MenuParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface MenuMapper extends Mapper<Menu> {

    List<Menu> queryPageByParam(MenuParam.MenuPageQuery pageQuery);

}