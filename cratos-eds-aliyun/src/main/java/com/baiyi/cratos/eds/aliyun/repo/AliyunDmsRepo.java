package com.baiyi.cratos.eds.aliyun.repo;

import com.aliyun.dms_enterprise20181101.models.*;
import com.baiyi.cratos.domain.util.BeanCopierUtil;
import com.baiyi.cratos.eds.aliyun.client.AliyunDmsClient;
import com.baiyi.cratos.eds.aliyun.client.MyDmsClient;
import com.baiyi.cratos.eds.aliyun.model.AliyunDms;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.exception.EdsRepoException;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/10 10:30
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AliyunDmsRepo {

    public static final int PAGE_SIZE = 50;

    public static AliyunDms.Tenant getTenant(EdsAliyunConfigModel.Aliyun aliyun) throws Exception {
        com.aliyun.dms_enterprise20181101.Client client = AliyunDmsClient.createClient(aliyun);
        GetUserActiveTenantRequest request = new GetUserActiveTenantRequest();
        GetUserActiveTenantResponse response = client.getUserActiveTenant(request);
        // response.getBody().getTenant();
        return BeanCopierUtil.copyProperties(Optional.ofNullable(response)
                .map(GetUserActiveTenantResponse::getBody)
                .orElseThrow(() -> new EdsRepoException("DMS tenant does not exist")), AliyunDms.Tenant.class);
    }

    public static List<AliyunDms.User> listUser(EdsAliyunConfigModel.Aliyun aliyun) throws Exception {
        long tid = Optional.of(aliyun)
                .map(EdsAliyunConfigModel.Aliyun::getDms)
                .map(EdsAliyunConfigModel.DMS::getTid)
                .orElse(-1L);
        if (tid == -1L) {
            return List.of();
        }
        return listUser(aliyun, tid);
    }

    public static List<AliyunDms.User> listUser(EdsAliyunConfigModel.Aliyun aliyun, Long tid) throws Exception {
        com.aliyun.dms_enterprise20181101.Client client = AliyunDmsClient.createClient(aliyun);
        ListUsersRequest request = new ListUsersRequest().setPageSize(PAGE_SIZE)
                .setTid(tid);
        int pageNumber = 1;
        List<AliyunDms.User> users = Lists.newArrayList();
        long totalCount = Long.MAX_VALUE;
        while (users.size() < totalCount) {
            request.setPageNumber(pageNumber);
            ListUsersResponse response = client.listUsers(request);
            ListUsersResponseBody body = response.getBody();
            if (body == null || body.getUserList() == null || body.getUserList()
                    .getUser() == null) {
                break;
            }
            List<ListUsersResponseBody.ListUsersResponseBodyUserListUser> list = body.getUserList()
                    .getUser();
            users.addAll(BeanCopierUtil.copyListProperties(list, AliyunDms.User.class));
            totalCount = body.getTotalCount() != null ? body.getTotalCount() : users.size();
            if (list.isEmpty()) {
                break;
            }
            pageNumber++;
        }
        return users;
    }

    public static void registerUser(EdsAliyunConfigModel.Aliyun aliyun, Long tid,
                                    AliyunDms.User user) throws Exception {
        MyDmsClient myDmsClient = AliyunDmsClient.createMyClient(aliyun);
        // DINGDING
        RegisterUserRequest request = new RegisterUserRequest();
        request.setTid(tid);
        request.setUserNick(user.getNickName());
        request.setMobile(user.getMobile());
        request.setUid(user.getUid());
        request.setRoleNames("USER");
        RegisterUserResponse response = myDmsClient.registerUser(request);
        if (!response.getBody()
                .getSuccess()) {
            EdsRepoException.runtime("DMS register user err: {}", response.getBody().errorMessage);
        }
    }

}
