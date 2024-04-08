package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.param.menu.MenuParam;
import com.baiyi.cratos.domain.view.menu.MyMenuVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/4/8 上午11:21
 * @Version 1.0
 */
public interface MyMenuFacade {

    List<MyMenuVO.MyMenu> queryMyMenu(MenuParam.QueryMyMenu queryMyMenu);

    List<MyMenuVO.MyMenu> queryUserMenu(String username, String lang);

}
