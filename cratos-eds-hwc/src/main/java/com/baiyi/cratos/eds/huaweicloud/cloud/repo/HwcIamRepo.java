package com.baiyi.cratos.eds.huaweicloud.cloud.repo;

import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.eds.core.config.model.EdsHwcConfigModel;
import com.baiyi.cratos.eds.huaweicloud.cloud.client.HwcIamClientBuilder;
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
            EdsHwcConfigModel.Hwc huaweicloud) throws ServiceResponseException {
        return listUsers(huaweicloud.getRegionId(), huaweicloud);
    }

    public static List<KeystoneListUsersResult> listUsers(String regionId,
                                                          EdsHwcConfigModel.Hwc hwc) throws ServiceResponseException {
        IamClient client = HwcIamClientBuilder.buildIamClient(regionId, hwc);
        KeystoneListUsersRequest request = new KeystoneListUsersRequest();
        KeystoneListUsersResponse response = client.keystoneListUsers(request);
        return response.getUsers();
    }

    public static KeystoneShowUserResult getUser(String regionId, EdsHwcConfigModel.Hwc hwc, String userId) {
        IamClient client = HwcIamClientBuilder.buildIamClient(regionId, hwc);
        KeystoneShowUserRequest request = new KeystoneShowUserRequest();
        request.setUserId(userId);
        KeystoneShowUserResponse response = client.keystoneShowUser(request);
        return response.getUser();
    }

    public static KeystoneCreateUserResult createUser(String regionId, EdsHwcConfigModel.Hwc hwc, User user,
                                                      String password) {
        IamClient client = HwcIamClientBuilder.buildIamClient(regionId, hwc);
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

    public static List<Credentials> listAccessKeys(String regionId, EdsHwcConfigModel.Hwc hwc, String userId) {
        IamClient client = HwcIamClientBuilder.buildIamClient(regionId, hwc);
        ListPermanentAccessKeysRequest request = new ListPermanentAccessKeysRequest();
        request.setUserId(userId);
        ListPermanentAccessKeysResponse response = client.listPermanentAccessKeys(request);
        return response.getCredentials();
    }

    public static KeystoneUpdateUserByAdminResult blockUser(String regionId, EdsHwcConfigModel.Hwc hwc, String userId) {
        IamClient client = HwcIamClientBuilder.buildIamClient(regionId, hwc);
        KeystoneUpdateUserByAdminRequest request = new KeystoneUpdateUserByAdminRequest();
        KeystoneUpdateUserByAdminRequestBody body = new KeystoneUpdateUserByAdminRequestBody();
        KeystoneUpdateUserOption user = new KeystoneUpdateUserOption();
        user.setEnabled(false);
        body.withUser(user);
        request.setUserId(userId);
        request.setBody(body);
        request.setBody(body);
        KeystoneUpdateUserByAdminResponse response = client.keystoneUpdateUserByAdmin(request);
        return response.getUser();
    }

}
