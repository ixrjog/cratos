package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.user.UserExtParam;
import com.baiyi.cratos.domain.view.user.UserVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/5 15:50
 * &#064;Version 1.0
 */
public interface UserExtFacade {

    DataTable<UserVO.User> queryExtUserPage(UserExtParam.UserExtPageQuery pageQuery);

}
