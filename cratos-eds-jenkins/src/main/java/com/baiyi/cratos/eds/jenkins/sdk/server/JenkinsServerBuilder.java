package com.baiyi.cratos.eds.jenkins.sdk.server;



import com.baiyi.cratos.eds.core.config.model.EdsJenkinsConfigModel;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @Author baiyi
 * @Date 2021/7/1 2:10 下午
 * @Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JenkinsServerBuilder {

    public static JenkinsServer build(EdsJenkinsConfigModel.Jenkins jenkins) throws URISyntaxException {
        return new JenkinsServer(new URI(jenkins.getUrl()),
                jenkins.getCred().getUsername(), jenkins.getCred().getToken());
    }

}