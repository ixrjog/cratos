package com.baiyi.cratos.eds.aliyun.client;

import com.aliyun.teaopenapi.models.Config;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.config.model.EdsAliyunConfigModel;
import lombok.NoArgsConstructor;

import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/2/26 11:36
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AliyunCasClient {

    public static com.aliyun.cas20200407.Client createClient(EdsConfigs.Aliyun aliyun) throws Exception {
        String casRegionId = Optional.of(aliyun)
                .map(EdsConfigs.Aliyun::getCas)
                .map(EdsAliyunConfigModel.CAS::getRegionId)
                .orElse(aliyun.getRegionId());
        com.aliyun.teaopenapi.models.Config config = new Config().setRegionId(casRegionId)
                .setAccessKeyId(aliyun.getCred()
                                        .getAccessKeyId())
                .setAccessKeySecret(aliyun.getCred()
                                            .getAccessKeySecret());
        // Endpoint 请参考 https://api.aliyun.com/product/cas
        if (casRegionId.equals("ap-southeast-1")) {
            config.endpoint = StringFormatter.format("cas.{}.aliyuncs.com", casRegionId);
        } else {
            config.endpoint = "cas.aliyuncs.com";
        }
        return new com.aliyun.cas20200407.Client(config);
    }

}
