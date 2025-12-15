package com.baiyi.cratos.eds.huaweicloud.cloud.client.cred;

import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.huaweicloud.sdk.core.auth.GlobalCredentials;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/10/22 09:50
 * &#064;Version 1.0
 */
public class HuaweicloudCredentialsBuilder {

    public static GlobalCredentials build(EdsConfigs.Hwc huaweicloud) {
        return new GlobalCredentials().withAk(huaweicloud.getCred()
                        .getAccessKey())
                .withSk(huaweicloud.getCred()
                        .getSecretKey())
                .withDomainId(huaweicloud.getCred()
                        .getUid());
    }

}
