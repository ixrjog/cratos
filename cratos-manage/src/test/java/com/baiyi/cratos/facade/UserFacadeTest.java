package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.user.UserParam;
import com.baiyi.cratos.service.RbacRoleService;
import com.baiyi.cratos.service.RbacUserRoleService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.wrapper.RbacRoleWrapper;
import com.google.common.base.Joiner;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/26 15:57
 * @Version 1.0
 */
public class UserFacadeTest extends BaseUnit {

    @Resource
    private UserFacade userFacade;

    @Resource
    private RbacFacade rbacFacade;

    @Resource
    private RbacRoleWrapper rbacRoleWrapper;

    @Resource
    private RbacRoleService rbacRoleService;

    @Resource
    private RbacUserRoleService rbacUserRoleService;

    @Resource
    private UserService userService;

    @Test
    void test() {
        UserParam.ResetPassword resetPassword = UserParam.ResetPassword.builder()
                .password("123456")
                .build();
        userFacade.resetUserPassword("baiyi-test", resetPassword);
    }


    @Test
    void test1() {
        List<User> users = userService.selectAll();
        String[] USER_TABLE_FIELD_NAME = {"Username", "Email", "Roles", "LastLogin", "Locked", "ExpiredTime", "CreateTime", "UpdateTime"};
        PrettyTable computerTable = PrettyTable.fieldNames(USER_TABLE_FIELD_NAME);

        for (User user : users) {

            List<String> roles = rbacUserRoleService.queryByUsername(user.getUsername())
                    .stream()
                    .map(e -> rbacRoleService.getById(e.getRoleId()))
                    .map(e -> e.getRoleName())
                    .toList();


            System.out.println(user.getUsername());

            computerTable.addRow(
                    user.getUsername(), user.getEmail(), Joiner.on(",")
                            .skipNulls()
                            .join(roles), user.getLastLogin(), user.getLocked(), user.getExpiredTime(),
                    user.getCreateTime(), user.getUpdateTime()
            );
        }


        System.out.println(computerTable.toString());

    }

}
