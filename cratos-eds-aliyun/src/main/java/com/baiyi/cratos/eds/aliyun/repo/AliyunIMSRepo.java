package com.baiyi.cratos.eds.aliyun.repo;


import com.aliyun.ims20190815.models.UpdateLoginProfileRequest;
import com.aliyun.ims20190815.models.UpdateLoginProfileResponse;
import com.baiyi.cratos.eds.aliyun.client.AliyunIMSClient;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import lombok.extern.slf4j.Slf4j;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/10 16:37
 * &#064;Version 1.0
 */
@Slf4j
public class AliyunIMSRepo {

    public static String updateLoginProfile(EdsConfigs.Aliyun aliyun, String ramUsername, String password,
                                            boolean passwordResetRequired) throws Exception {
        UpdateLoginProfileRequest request = new UpdateLoginProfileRequest();
        request.setUserPrincipalName(ramUsername);
        request.setPassword(password);
        request.setPasswordResetRequired(passwordResetRequired);
        request.setStatus("Active");
        com.aliyun.ims20190815.Client client = AliyunIMSClient.createClient(aliyun);
        UpdateLoginProfileResponse response = client.updateLoginProfile(request);
        return response.getBody()
                .getRequestId();
    }

}
