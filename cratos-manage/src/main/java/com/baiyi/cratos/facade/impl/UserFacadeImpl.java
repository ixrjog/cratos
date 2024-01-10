package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.user.UserParam;
import com.baiyi.cratos.domain.view.user.UserVO;
import com.baiyi.cratos.facade.UserFacade;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.wrapper.UserWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/10 10:15
 * @Version 1.0
 */
@Component
@AllArgsConstructor
public class UserFacadeImpl implements UserFacade {

    private final UserService userService;

    private final UserWrapper userWrapper;

    @Override
    public DataTable<UserVO.User> queryUserPage(UserParam.UserPageQuery pageQuery) {
        DataTable<User> table = userService.queryUserPage(pageQuery);
        return userWrapper.wrapToTarget(table);
    }

}