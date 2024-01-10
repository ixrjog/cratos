package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.user.UserParam;
import com.baiyi.cratos.domain.view.user.UserVO;

/**
 * @Author baiyi
 * @Date 2024/1/10 10:15
 * @Version 1.0
 */
public interface UserFacade {

    DataTable<UserVO.User> queryUserPage(UserParam.UserPageQuery pageQuery);

    void addUser(UserParam.AddUser addUser);

}
