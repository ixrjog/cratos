package com.baiyi.cratos.eds.aliyun.client;

import com.aliyun.teaopenapi.models.Config;
import com.baiyi.cratos.eds.core.config.model.EdsAliyunConfigModel;
import lombok.NoArgsConstructor;

import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/10 10:41
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AliyunDmsClient {

    public static com.aliyun.dms_enterprise20181101.Client createClient(
            EdsAliyunConfigModel.Aliyun aliyun) throws Exception {
        return new com.aliyun.dms_enterprise20181101.Client(getConfig(aliyun));
    }

    public static MyDmsClient createMyClient(EdsAliyunConfigModel.Aliyun aliyun) throws Exception {
        return new MyDmsClient(getConfig(aliyun));
    }

    private static Config getConfig(EdsAliyunConfigModel.Aliyun aliyun) {
        return new Config().setAccessKeyId(aliyun.getCred()
                        .getAccessKeyId())
                .setAccessKeySecret(aliyun.getCred()
                        .getAccessKeySecret())
                .setEndpoint(Optional.of(aliyun)
                        .map(EdsAliyunConfigModel.Aliyun::getDms)
                        .map(EdsAliyunConfigModel.DMS::getEndpoint)
                        .orElse(EdsAliyunConfigModel.DMS_ENDPOINT));
    }

}
