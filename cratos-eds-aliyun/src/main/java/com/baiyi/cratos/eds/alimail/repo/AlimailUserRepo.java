package com.baiyi.cratos.eds.alimail.repo;

import com.baiyi.cratos.common.exception.EdsIdentityException;
import com.baiyi.cratos.eds.alimail.model.AlimailUser;
import com.baiyi.cratos.eds.alimail.param.AlimailUserParam;
import com.baiyi.cratos.eds.alimail.service.AlimailService;
import com.baiyi.cratos.eds.alimail.service.AlimailServiceFactory;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/12 09:31
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AlimailUserRepo {

    public static List<AlimailUser.User> listUsersOfDepartment(EdsConfigs.Alimail alimail, String deptId) {
        AlimailService alimailService = AlimailServiceFactory.createAuthenticatedService(alimail);
        List<AlimailUser.User> users = Lists.newArrayList();
        int offset = 0;
        final int limit = 100;
        AlimailUser.ListUsersOfDepartmentResult result;
        do {
            result = alimailService.listUsersOfDepartment(deptId, limit, offset);
            if (!CollectionUtils.isEmpty(result.getUsers())) {
                users.addAll(result.getUsers());
                offset += limit;
            }
        } while (!CollectionUtils.isEmpty(result.getUsers()));
        return users;
    }

    public static void freezeUser(EdsConfigs.Alimail alimail, String userId) {
        AlimailService alimailService = AlimailServiceFactory.createAuthenticatedService(alimail);
        try {
            alimailService.freezeUser(userId, AlimailUserParam.UpdateUser.FREEZE_USER);
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                // 用户不存在
                return;
            }
            throw new EdsIdentityException(e.getMessage());
        }
    }

    public static AlimailUser.ResetPasswordResult resetPassword(EdsConfigs.Alimail alimail, String userId,
                                                         String newPassword) {
        AlimailService alimailService = AlimailServiceFactory.createAuthenticatedService(alimail);
        AlimailUserParam.ResetPassword resetPassword = AlimailUserParam.ResetPassword.builder()
                .password(newPassword)
                .build();
        return alimailService.resetPassword(userId, resetPassword);
    }

}
