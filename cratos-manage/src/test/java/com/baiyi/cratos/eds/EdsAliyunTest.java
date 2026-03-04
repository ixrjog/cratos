package com.baiyi.cratos.eds;

import com.aliyun.cas20200407.models.ListCertificatesResponseBody;
import com.aliyun.cas20200407.models.ListCloudResourcesResponseBody;
import com.aliyun.sdk.service.alidns20150109.models.DescribeDomainRecordsResponseBody;
import com.aliyun.sdk.service.kms20160120.models.GetSecretValueResponseBody;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ram.model.v20150501.GetUserResponse;
import com.aliyuncs.ram.model.v20150501.UpdateUserResponse;
import com.baiyi.cratos.common.util.PhoneNumberUtils;
import com.baiyi.cratos.domain.generator.AcmeCertificate;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.eds.aliyun.repo.*;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.acme.AcmeCertificateService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/25 13:38
 * &#064;Version 1.0
 */
public class EdsAliyunTest extends BaseEdsTest<EdsConfigs.Aliyun> {

    @Resource
    private AliyunTagRepo aliyunTagRepo;

    @Resource
    private AliyunCertRepo aliyunCertRepo;

    @Resource
    private AcmeCertificateService acmeCertificateService;

    @Resource
    private EdsAssetService edsAssetService;

    @Resource
    private EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;

    @Resource
    private UserService userService;

    @Resource
    private AliyunRamUserRepo aliyunRamUserRepo;

    @Test
    void test2() {
        EdsConfigs.Aliyun aliyun = getConfig(2);
        Optional<GetSecretValueResponseBody> opt = AliyunKmsRepo.getSecretValue(
                "kms.eu-central-1.aliyuncs.com", aliyun,
                "acs:kms:eu-central-1:1859120988191686:secret/daily_finance-switch-channel_selcom_palmpay_secretKey"
        );
        System.out.println(opt.get());
    }

    @Test
    void test3() {
        EdsConfigs.Aliyun aliyun = getConfig(2);
        List<DescribeDomainRecordsResponseBody.Record> records = AliyunDnsRepo.describeDomainRecords(
                aliyun, "palmpay-inc.com");
        System.out.println(records);
    }

    @Test
    void test4() {
        EdsConfigs.Aliyun aliyun = getConfig(25);
        List<EdsAsset> assets = edsAssetService.queryInstanceAssets(114, EdsAssetTypeEnum.ALIYUN_RAM_USER.name());
        EdsInstanceProviderHolder<EdsConfigs.Aliyun, GetUserResponse.User> holder = (EdsInstanceProviderHolder<EdsConfigs.Aliyun, GetUserResponse.User>) edsInstanceProviderHolderBuilder.newHolder(
                114, EdsAssetTypeEnum.ALIYUN_RAM_USER.name());
        for (EdsAsset asset : assets) {
            if (asset.getAssetKey()
                    .startsWith("ak-")) {
                continue;
            }
            GetUserResponse.User ramUser = holder.getProvider()
                    .assetLoadAs(asset.getOriginalModel());
            if (!StringUtils.hasText(ramUser.getMobilePhone())) {
                System.out.println("用户手机号不存在: user=" + ramUser.getUserName());
                User user = userService.getByUsername(asset.getAssetKey());
                if (user == null) {
                    continue;
                }
                String phone = PhoneNumberUtils.convertPhoneNumber(user.getMobilePhone());
                if (PhoneNumberUtils.isValidPhoneNumber(phone)) {
                    try {
                        UpdateUserResponse.User updateUser = aliyunRamUserRepo.updateUser(
                                aliyun.getRegionId(), aliyun, ramUser.getUserName(), user.getEmail(), phone);
                        System.out.println(
                                updateUser.getUserName() + "  " + updateUser.getMobilePhone() + "  " + updateUser.getEmail());
                    } catch (ClientException ce) {
                        ce.printStackTrace();
                    }
                }
            }

        }
    }

    @Test
    void test5() throws Exception {
        EdsConfigs.Aliyun aliyun = getConfig(2);
        AcmeCertificate acmeCertificate = acmeCertificateService.getById(1);
        String cert = acmeCertificate.getCertificate() + "\n" + acmeCertificate.getCertificateChain();
        Long certId = aliyunCertRepo.uploadUserCertificate(aliyun, "test111", cert, acmeCertificate.getPrivateKey());
        System.out.println(certId);
    }

    @Test
    void test6() throws Exception {
        EdsConfigs.Aliyun aliyun = getConfig(2);
        List<ListCloudResourcesResponseBody.ListCloudResourcesResponseBodyData> result = aliyunCertRepo.listCloudResources(
                aliyun, 21776764L);
        System.out.println(result);
    }


    @Test
    void test7() throws Exception {
        EdsConfigs.Aliyun aliyun = getConfig(2);
        ListCertificatesResponseBody.ListCertificatesResponseBodyCertificateList result = aliyunCertRepo.queryCertificateByName(
                aliyun, "2026-12-31palmpay.app");
        System.out.println(result);
    }


    @Test
    void test8() throws Exception {
        EdsConfigs.Aliyun aliyun = getConfig(2);
        List<ListCertificatesResponseBody.ListCertificatesResponseBodyCertificateList> result = aliyunCertRepo.listCertificates(
                aliyun);
        int sum = 0;
        String x = "";

        for (ListCertificatesResponseBody.ListCertificatesResponseBodyCertificateList cert : result) {
            try {
                Thread.sleep(2000);

                List<ListCloudResourcesResponseBody.ListCloudResourcesResponseBodyData> resources = aliyunCertRepo.listCloudResources(
                        aliyun, Long.valueOf(cert.getCertificateId()));

                x = x + "\n" + StringFormatter.arrayFormat(
                        "{}({}) certId={}; resources: {}", cert.getCertificateName(),
                        cert.getDomain() + "," + cert.getCommonName(), cert.getCertificateId(), resources.size()
                );
                sum = sum + resources.size();
            } catch (Exception ex) {

                ex.printStackTrace();
                System.out.println(cert.getCertificateName());

            }
        }
        System.out.println(x);
        System.out.println(sum);
    }


}

