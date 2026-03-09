package com.baiyi.cratos.eds.ucloud.global.client;

import cn.ucloud.common.config.Config;
import cn.ucloud.common.credential.Credential;
import cn.ucloud.uhost.client.UHostClient;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/6 10:39
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class UcloudUHostClient {

    private static final Logger logger = LoggerFactory.getLogger(UcloudUHostClient.class);

    private static final String API = "https://api.ucloud.cn";

    public static UHostClient createClient(String regionId, EdsConfigs.Ucloud ucloud) throws Exception {
        Config config = new Config();
        config.setRegion(regionId);
        config.setLogger(logger);
        config.setBaseUrl(Optional.of(ucloud)
                                  .map(EdsConfigs.Ucloud::getApi)
                                  .orElse(API));
        Credential credential = new Credential(
                ucloud.getCred()
                        .getPublicKey(), ucloud.getCred()
                        .getPrivateKey()
        );
        return new UHostClient(config, credential);
    }

}
