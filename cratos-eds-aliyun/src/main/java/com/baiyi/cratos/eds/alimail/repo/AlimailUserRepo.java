package com.baiyi.cratos.eds.alimail.repo;

import com.baiyi.cratos.eds.alimail.client.AlimailTokenClient;
import com.baiyi.cratos.eds.alimail.model.AlimailToken;
import com.baiyi.cratos.eds.alimail.model.AlimailUser;
import com.baiyi.cratos.eds.alimail.service.AlimailService;
import com.baiyi.cratos.eds.alimail.service.AlimailServiceFactory;
import com.baiyi.cratos.eds.core.config.EdsAlimailConfigModel;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
        int limit = 100;
        while (true) {
            AlimailUser.ListUsersOfDepartmentResult result= alimailService.listUsersOfDepartment(token.toBearer(), deptId, limit, offset);
            if (CollectionUtils.isEmpty(result.getUsers())) {
                break;
            }
            users.addAll(result.getUsers());
            offset += limit;
        }
        return users;
    }

}
