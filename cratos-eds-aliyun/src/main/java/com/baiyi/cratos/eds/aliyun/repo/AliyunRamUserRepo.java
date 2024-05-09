package com.baiyi.cratos.eds.aliyun.repo;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ram.model.v20150501.*;
import com.baiyi.cratos.eds.aliyun.client.AliyunClient;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/9 下午3:59
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliyunRamUserRepo {

    public static final int PAGE_SIZE = 50;

    private final AliyunClient aliyunClient;

    public List<ListUsersResponse.User> listUsers(EdsAliyunConfigModel.Aliyun aliyun) throws ClientException {
        List<ListUsersResponse.User> userList = Lists.newArrayList();
        String marker;
        ListUsersRequest request = new ListUsersRequest();
        request.setMaxItems(PAGE_SIZE);
        do {
            ListUsersResponse response = aliyunClient.getAcsResponse(aliyun.getRegionId(), aliyun, request);
            userList.addAll(response.getUsers());
            marker = response.getMarker();
            request.setMarker(marker);
        } while (StringUtils.isNotBlank(marker));
        return userList;
    }

    public GetUserResponse.User getUser(EdsAliyunConfigModel.Aliyun aliyun, String ramUsername) throws ClientException {
        GetUserRequest request = new GetUserRequest();
        request.setUserName(ramUsername);
        GetUserResponse response = aliyunClient.getAcsResponse(aliyun.getRegionId(), aliyun, request);
        return response.getUser();
    }

    public List<ListEntitiesForPolicyResponse.User> listUsersForPolicy(EdsAliyunConfigModel.Aliyun aliyun,
                                                                       String policyName,
                                                                       String policyType) throws ClientException {
        ListEntitiesForPolicyRequest request = new ListEntitiesForPolicyRequest();
        request.setPolicyName(policyName);
        request.setPolicyType(policyType);
        ListEntitiesForPolicyResponse response = aliyunClient.getAcsResponse(aliyun.getRegionId(), aliyun, request);
        return response == null ? Collections.emptyList() : response.getUsers();
    }

}
