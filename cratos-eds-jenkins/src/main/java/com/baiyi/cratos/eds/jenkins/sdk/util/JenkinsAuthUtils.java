package com.baiyi.cratos.eds.jenkins.sdk.util;


import com.baiyi.cratos.common.cred.Authorization;
import com.baiyi.cratos.eds.core.config.EdsJenkinsConfigModel;

/**
 * @Author baiyi
 * @Date 2022/1/5 11:00 AM
 * @Version 1.0
 */
public class JenkinsAuthUtils {

    private JenkinsAuthUtils() {
    }

    public static Authorization.Credential buildAuthentication(EdsJenkinsConfigModel.Jenkins jenkins) {
        return Authorization.Credential.builder()
                .username(jenkins.getCred()
                        .getUsername())
                .password(jenkins.getCred()
                        .getToken())
                .build();
    }

}