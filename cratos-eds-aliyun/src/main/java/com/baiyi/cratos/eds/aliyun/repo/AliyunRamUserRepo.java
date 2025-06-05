package com.baiyi.cratos.eds.aliyun.repo;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ram.model.v20150501.*;
import com.baiyi.cratos.common.util.ValidationUtils;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.eds.aliyun.client.common.AliyunClient;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static com.baiyi.cratos.domain.constant.Global.CREATED_BY;

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
    public static final boolean NO_PASSWORD_RESET_REQUIRED = false;
    private final AliyunClient aliyunClient;

    public static final boolean CREATE_LOGIN_PROFILE = true;
    public static final boolean ENABLE_MFA = true;

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

    public CreateUserResponse.User createUser(String regionId, EdsAliyunConfigModel.Aliyun aliyun, User user,
                                              String password, boolean createLoginProfile,
                                              boolean enableMFA) throws ClientException {
        CreateUserResponse.User createUser = createUser(regionId, aliyun, user);
        if (createLoginProfile) {
            createLoginProfile(regionId, aliyun, user, password, NO_PASSWORD_RESET_REQUIRED, enableMFA);
        }
        return createUser;
    }

    public CreateUserResponse.User createUser(String regionId, EdsAliyunConfigModel.Aliyun aliyun, String ramUsername,
                                              String password, boolean createLoginProfile,
                                              boolean enableMFA) throws ClientException {
        CreateUserResponse.User createUser = createUser(regionId, aliyun, ramUsername);
        if (createLoginProfile) {
            createLoginProfile(regionId, aliyun, ramUsername, password, NO_PASSWORD_RESET_REQUIRED, enableMFA);
        }
        return createUser;
    }

    public CreateUserResponse.User createUser(String regionId, EdsAliyunConfigModel.Aliyun aliyun,
                                              String username) throws ClientException {
        CreateUserRequest request = new CreateUserRequest();
        request.setUserName(username);
        request.setDisplayName(username);
        request.setComments(CREATED_BY);
        return aliyunClient.getAcsResponse(regionId, aliyun, request)
                .getUser();
    }


    /**
     * 开通控制台登录
     *
     * @param regionId
     * @param aliyun
     * @param user
     * @param passwordResetRequired
     * @return
     * @throws ClientException
     */
    private CreateLoginProfileResponse.LoginProfile createLoginProfile(String regionId,
                                                                       EdsAliyunConfigModel.Aliyun aliyun, User user,
                                                                       String password, boolean passwordResetRequired,
                                                                       boolean mFABindRequired) throws ClientException {
        CreateLoginProfileRequest request = new CreateLoginProfileRequest();
        request.setUserName(user.getUsername());
        request.setPassword(password);
        request.setPasswordResetRequired(passwordResetRequired);
        request.setMFABindRequired(mFABindRequired);
        return aliyunClient.getAcsResponse(regionId, aliyun, request)
                .getLoginProfile();
    }

    public CreateLoginProfileResponse.LoginProfile createLoginProfile(String regionId,
                                                                       EdsAliyunConfigModel.Aliyun aliyun,
                                                                       String ramUsername, String password,
                                                                       boolean passwordResetRequired,
                                                                       boolean mFABindRequired) throws ClientException {
        CreateLoginProfileRequest request = new CreateLoginProfileRequest();
        request.setUserName(ramUsername);
        request.setPassword(password);
        request.setPasswordResetRequired(passwordResetRequired);
        request.setMFABindRequired(mFABindRequired);
        return aliyunClient.getAcsResponse(regionId, aliyun, request)
                .getLoginProfile();
    }

    public String updateLoginProfile(EdsAliyunConfigModel.Aliyun aliyun, String ramUsername, String password,
                                     boolean passwordResetRequired) throws ClientException {
        UpdateLoginProfileRequest request = new UpdateLoginProfileRequest();
        request.setUserName(ramUsername);
        request.setPassword(password);
        request.setPasswordResetRequired(passwordResetRequired);
        UpdateLoginProfileResponse response = aliyunClient.getAcsResponse(aliyun.getRegionId(), aliyun, request);
        return response.getRequestId();
    }

    private CreateUserResponse.User createUser(String regionId, EdsAliyunConfigModel.Aliyun aliyun,
                                               User user) throws ClientException {
        CreateUserRequest request = new CreateUserRequest();
        request.setUserName(user.getUsername());
        request.setDisplayName(user.getDisplayName());
        if (ValidationUtils.isPhone(user.getMobilePhone())) {
            request.setMobilePhone("86-" + user.getMobilePhone());
        }
        if (!StringUtils.isEmpty(user.getEmail())) {
            request.setEmail(user.getEmail());
        }
        request.setComments(CREATED_BY);
        return aliyunClient.getAcsResponse(regionId, aliyun, request)
                .getUser();
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

    public GetLoginProfileResponse.LoginProfile getLoginProfile(String regionId, EdsAliyunConfigModel.Aliyun aliyun,
                                                                String username) throws ClientException {
        GetLoginProfileRequest request = new GetLoginProfileRequest();
        request.setUserName(username);
        GetLoginProfileResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
        return response.getLoginProfile();
    }

    public boolean deleteLoginProfile(String regionId, EdsAliyunConfigModel.Aliyun aliyun, String username) {
        DeleteLoginProfileRequest request = new DeleteLoginProfileRequest();
        request.setUserName(username);
        try {
            DeleteLoginProfileResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
            if (!StringUtils.isEmpty(response.getRequestId())) {
                return true;
            }
        } catch (ClientException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public GetUserMFAInfoResponse.MFADevice getUserMFAInfo(EdsAliyunConfigModel.Aliyun aliyun,
                                                           String ramUsername) throws ClientException {
        GetUserMFAInfoRequest request = new GetUserMFAInfoRequest();
        request.setUserName(ramUsername);
        GetUserMFAInfoResponse response = aliyunClient.getAcsResponse(aliyun.getRegionId(), aliyun, request);
        return response.getMFADevice();
    }

    /**
     * MFA 设备需要先解除绑定才能删除
     * @param aliyun
     * @param ramUsername
     * @return
     * @throws ClientException
     */
    public UnbindMFADeviceResponse.MFADevice unbindVirtualMFADevice(EdsAliyunConfigModel.Aliyun aliyun,
                                                                    String ramUsername) throws ClientException {
        UnbindMFADeviceRequest request = new UnbindMFADeviceRequest();
        request.setUserName(ramUsername);
        UnbindMFADeviceResponse response = aliyunClient.getAcsResponse(aliyun.getRegionId(), aliyun, request);
        return response.getMFADevice();
    }

    public void deleteVirtualMFADevice(EdsAliyunConfigModel.Aliyun aliyun, String serialNumber) throws ClientException {
        DeleteVirtualMFADeviceRequest request = new DeleteVirtualMFADeviceRequest();
        request.setSerialNumber(serialNumber);
        aliyunClient.getAcsResponse(aliyun.getRegionId(), aliyun, request);
    }

}
