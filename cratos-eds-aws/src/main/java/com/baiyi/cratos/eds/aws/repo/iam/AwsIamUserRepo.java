package com.baiyi.cratos.eds.aws.repo.iam;

import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.model.ListUsersRequest;
import com.amazonaws.services.identitymanagement.model.ListUsersResult;
import com.amazonaws.services.identitymanagement.model.User;
import com.baiyi.cratos.eds.aws.service.AmazonIdentityManagementService;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/17 15:47
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AwsIamUserRepo {

    public List<User> listUsers(EdsAwsConfigModel.Aws aws) {
        ListUsersRequest request = new ListUsersRequest();
        List<User> users = Lists.newArrayList();
        AmazonIdentityManagement iamClient = AmazonIdentityManagementService.buildAmazonIdentityManagement(aws);
        ListUsersResult result;
        do {
            result = iamClient.listUsers(request);
            users.addAll(result.getUsers());
            request.setMarker(result.getMarker());
        } while (result.getIsTruncated());
        return users;
    }

}
