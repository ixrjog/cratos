package com.baiyi.cratos.facade;

import com.baiyi.cratos.HasSetValid;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.user.UserExtParam;
import com.baiyi.cratos.domain.param.http.user.UserParam;
import com.baiyi.cratos.domain.view.credential.CredentialVO;
import com.baiyi.cratos.domain.view.user.UserVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/10 10:15
 * @Version 1.0
 */
public interface UserFacade extends HasSetValid {

    DataTable<UserVO.User> queryUserPage(UserParam.UserPageQuery pageQuery);

    DataTable<UserVO.User> queryCommandExecUserPage(UserParam.CommandExecUserPageQuery pageQuery);

    DataTable<UserVO.User> queryExtUserPage(UserExtParam.UserExtPageQuery pageQuery);

    User addUser(UserParam.AddUser addUser);

    UserVO.User getUserByUsername(String username);

    void resetUserPassword(UserParam.ResetPassword resetPassword);

    void resetUserPassword(String username, UserParam.ResetPassword resetPassword);

    void updateUser(UserParam.UpdateUser updateUser);

    void updateUser(UserParam.UpdateMy updateMy);

    List<CredentialVO.Credential> queryMySshKey();

    List<CredentialVO.Credential> querySshKey(UserParam.QuerySshKey querySshKey);

    void addSshKey(UserParam.AddSshKey addSshKey);

    void addMySshKey(UserParam.AddMySshKey addMySshKey);

}
