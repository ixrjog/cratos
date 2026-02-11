package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.Menu;
import com.baiyi.cratos.domain.param.http.menu.MenuParam;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface MenuMapper extends Mapper<Menu> {

    List<Menu> queryPageByParam(MenuParam.MenuPageQuery pageQuery);

}