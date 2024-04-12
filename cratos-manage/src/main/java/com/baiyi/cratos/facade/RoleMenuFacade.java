package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.view.menu.RoleMenuVO;

/**
 * @Author baiyi
 * @Date 2024/4/12 下午1:41
 * @Version 1.0
 */
public interface RoleMenuFacade {

    RoleMenuVO.RoleMenu getRoleMenu(int roleId, String lang);

}
