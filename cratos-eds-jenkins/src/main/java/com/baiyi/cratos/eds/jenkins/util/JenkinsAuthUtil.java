package com.baiyi.cratos.eds.jenkins.util;

//import com.baiyi.opscloud.common.datasource.JenkinsConfig;
//import com.baiyi.opscloud.common.util.StringFormatter;
//import com.baiyi.opscloud.core.model.Authentication;

import com.baiyi.cratos.common.cred.Authorization;
import com.baiyi.cratos.eds.core.config.EdsJenkinsConfigModel;

/**
 * @Author baiyi
 * @Date 2022/1/5 11:00 AM
 * @Version 1.0
 */
public class JenkinsAuthUtil {

    private JenkinsAuthUtil() {
    }

    public static Authorization.Credential buildAuthentication(EdsJenkinsConfigModel.Jenkins jenkins) {
//        return Authentication.builder()
//                .token(Joiner.on(" ")
//                        .join("Basic", toAuthBasic(jenkins)))
//                .build();
        return Authorization.Credential.builder()
                .username(jenkins.getCred()
                        .getUsername())
                .password(jenkins.getCred()
                        .getToken())
                .build();
    }

}