package com.baiyi.cratos.eds.huaweicloud.repo;

import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.eds.core.config.EdsHuaweicloudConfigModel;
import com.baiyi.cratos.eds.huaweicloud.client.HwcIamClientBuilder;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.iam.v3.IamClient;
import com.huaweicloud.sdk.iam.v3.model.*;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/17 下午1:48
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class HwcIamRepo {

    public static List<KeystoneListUsersResult> listUsers(
            EdsHuaweicloudConfigModel.Huaweicloud huaweicloud) throws ServiceResponseException {
        return listUsers(huaweicloud.getRegionId(), huaweicloud);
    }

    public static List<KeystoneListUsersResult> listUsers(String regionId,
                                                          EdsHuaweicloudConfigModel.Huaweicloud huaweicloud) throws ServiceResponseException {
        IamClient client = HwcIamClientBuilder.buildIamClient(regionId, huaweicloud);
        KeystoneListUsersRequest request = new KeystoneListUsersRequest();
        KeystoneListUsersResponse response = client.keystoneListUsers(request);
        return response.getUsers();
    }

    public static KeystoneShowUserResult getUser(String regionId, EdsHuaweicloudConfigModel.Huaweicloud huaweicloud,
                                                 String userId) {
        IamClient client = HwcIamClientBuilder.buildIamClient(regionId, huaweicloud);
        KeystoneShowUserRequest request = new KeystoneShowUserRequest();
        request.setUserId(userId);
        KeystoneShowUserResponse response = client.keystoneShowUser(request);
        return response.getUser();
    }

    public static KeystoneCreateUserResult createUser(String regionId,
                                                      EdsHuaweicloudConfigModel.Huaweicloud huaweicloud, User user ,String password) {
        IamClient client = HwcIamClientBuilder.buildIamClient(regionId, huaweicloud);
        KeystoneCreateUserRequest request = new KeystoneCreateUserRequest();
        KeystoneCreateUserRequestBody body = new KeystoneCreateUserRequestBody();
        KeystoneCreateUserOption createUserOption = new KeystoneCreateUserOption();
        createUserOption.setName(user.getUsername());
        createUserOption.setPassword(password);
        createUserOption.setEnabled(true);
        createUserOption.setDescription(user.getComment());
        body.setUser(createUserOption);
        request.setBody(body);
        KeystoneCreateUserResponse response = client.keystoneCreateUser(request);
        return response.getUser();
    }

}
