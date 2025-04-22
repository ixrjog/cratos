package com.baiyi.cratos.eds.aws.repo.iam;

import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.model.*;
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

    public static final boolean NO_PASSWORD_RESET_REQUIRED = false;

    public User getUser(EdsAwsConfigModel.Aws aws, String userName) {
        GetUserRequest request = new GetUserRequest();
        request.setUserName(userName);
        GetUserResult result = AmazonIdentityManagementService.buildAmazonIdentityManagement(aws)
                .getUser(request);
        return result.getUser();
    }

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

    /**
     * https://docs.aws.amazon.com/zh_cn/IAM/latest/APIReference/API_CreateUser.html
     *
     * @param aws
     */
    public com.amazonaws.services.identitymanagement.model.User createUser(EdsAwsConfigModel.Aws aws,
                                                                           com.baiyi.cratos.domain.generator.User user,
                                                                           String password,
                                                                           boolean createLoginProfile) {
        CreateUserRequest request = new CreateUserRequest();
        request.setUserName(user.getUsername());
        CreateUserResult result = AmazonIdentityManagementService.buildAmazonIdentityManagement(aws)
                .createUser(request);
        if (createLoginProfile) {
            this.createLoginProfile(aws, user, password, NO_PASSWORD_RESET_REQUIRED);
//            try {
//                this.createLoginProfile(aws, user, NO_PASSWORD_RESET_REQUIRED);
//            } catch (PasswordPolicyViolationException e) {
//                throw new CreateUserException(e.getMessage());
//            }
        }
        return result.getUser();
    }

    private LoginProfile createLoginProfile(EdsAwsConfigModel.Aws aws, com.baiyi.cratos.domain.generator.User user,
                                            String password, boolean passwordResetRequired) {
        CreateLoginProfileRequest request = new CreateLoginProfileRequest();
        request.setUserName(user.getUsername());
        request.setPassword(password);
        request.setPasswordResetRequired(passwordResetRequired);
        CreateLoginProfileResult result = AmazonIdentityManagementService.buildAmazonIdentityManagement(aws)
                .createLoginProfile(request);
        return result.getLoginProfile();
    }

    public void deleteLoginProfile(EdsAwsConfigModel.Aws aws, String userName) {
        DeleteLoginProfileRequest request = new DeleteLoginProfileRequest();
        request.setUserName(userName);
        DeleteLoginProfileResult result = AmazonIdentityManagementService.buildAmazonIdentityManagement(aws)
                .deleteLoginProfile(request);
    }

}
