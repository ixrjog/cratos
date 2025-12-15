package com.baiyi.cratos.eds;

import com.aliyun.sdk.service.alidns20150109.models.DescribeDomainRecordsResponseBody;
import com.aliyun.sdk.service.kms20160120.models.GetSecretValueResponseBody;
import com.baiyi.cratos.eds.aliyun.repo.AliyunDnsRepo;
import com.baiyi.cratos.eds.aliyun.repo.AliyunKmsRepo;
import com.baiyi.cratos.eds.aliyun.repo.AliyunTagRepo;
import com.baiyi.cratos.eds.core.config.model.EdsAliyunConfigModel;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/25 13:38
 * &#064;Version 1.0
 */
public class EdsAliyunTest extends BaseEdsTest<EdsAliyunConfigModel.Aliyun> {

    @Resource
    private AliyunTagRepo aliyunTagRepo;

    @Test
    void test2() {
        EdsAliyunConfigModel.Aliyun aliyun = getConfig(2);
        Optional<GetSecretValueResponseBody> opt = AliyunKmsRepo.getSecretValue(
                "kms.eu-central-1.aliyuncs.com", aliyun,
                "acs:kms:eu-central-1:1859120988191686:secret/daily_finance-switch-channel_selcom_palmpay_secretKey"
        );
        System.out.println(opt.get());
    }

    @Test
    void test3() {
        EdsAliyunConfigModel.Aliyun aliyun = getConfig(2);
        List<DescribeDomainRecordsResponseBody.Record> records = AliyunDnsRepo.describeDomainRecords(
                aliyun, "palmpay-inc.com");
        System.out.println(records);
    }

}

