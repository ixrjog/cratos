package com.baiyi.cratos.eds.alimail.repo;

import com.baiyi.cratos.common.exception.EdsIdentityException;
import com.baiyi.cratos.eds.alimail.client.AlimailTokenClient;
import com.baiyi.cratos.eds.alimail.model.AlimailToken;
import com.baiyi.cratos.eds.alimail.model.AlimailUser;
import com.baiyi.cratos.eds.alimail.param.AlimailUserParam;
import com.baiyi.cratos.eds.alimail.service.AlimailService;
import com.baiyi.cratos.eds.alimail.service.AlimailServiceFactory;
import com.baiyi.cratos.eds.core.config.EdsAlimailConfigModel;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/12 09:31
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AlimailUserRepo {

    private final AlimailTokenClient alimailTokenClient;

    public List<AlimailUser.User> listUsersOfDepartment(EdsAlimailConfigModel.Alimail alimail, String deptId) {
        AlimailService alimailService = AlimailServiceFactory.createAlimailService(alimail);
        AlimailToken.Token token = alimailTokenClient.getToken(alimail);
        List<AlimailUser.User> users = Lists.newArrayList();
        int offset = 0;
        final int limit = 100;
        AlimailUser.ListUsersOfDepartmentResult result;
        do {
            result = alimailService.listUsersOfDepartment(token.toBearer(), deptId, limit, offset);
            if (!CollectionUtils.isEmpty(result.getUsers())) {
                users.addAll(result.getUsers());
                offset += limit;
            }
        } while (!CollectionUtils.isEmpty(result.getUsers()));
        return users;
    }

    public void freezeUser(EdsAlimailConfigModel.Alimail alimail, String userId) {
        AlimailService alimailService = AlimailServiceFactory.createAlimailService(alimail);
        AlimailToken.Token token = alimailTokenClient.getToken(alimail);
        try {
            alimailService.freezeUser(token.toBearer(), userId, AlimailUserParam.UpdateUser.FREEZE_USER);
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                // 用户不存在
                return;
            }
            throw new EdsIdentityException(e.getMessage());
        }
    }

}
