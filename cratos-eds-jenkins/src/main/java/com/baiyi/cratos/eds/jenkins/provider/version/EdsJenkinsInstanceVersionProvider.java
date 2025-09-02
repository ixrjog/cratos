package com.baiyi.cratos.eds.jenkins.provider.version;

import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsJenkinsConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.version.IEdsInstanceVersionProvider;
import com.baiyi.cratos.eds.jenkins.helper.JenkinsVersion;
import com.baiyi.cratos.eds.jenkins.sdk.server.JenkinsServer;
import com.baiyi.cratos.eds.jenkins.sdk.server.JenkinsServerBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/2 10:09
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.JENKINS)
public class EdsJenkinsInstanceVersionProvider implements IEdsInstanceVersionProvider<EdsJenkinsConfigModel.Jenkins> {

    @Override
    public String getVersion(ExternalDataSourceInstance<EdsJenkinsConfigModel.Jenkins> instance) {
        try (JenkinsServer jenkinsServer = JenkinsServerBuilder.build(instance.getEdsConfigModel())) {
            JenkinsVersion jenkinsVersion = jenkinsServer.getVersion();
            return jenkinsVersion.getLiteralVersion();
        } catch (URISyntaxException uriSyntaxException) {
            return "--";
        }
    }

}