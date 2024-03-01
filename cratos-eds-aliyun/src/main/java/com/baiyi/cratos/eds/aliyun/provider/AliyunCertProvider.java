package com.baiyi.cratos.eds.aliyun.provider;

import com.aliyun.cas20200407.models.ListUserCertificateOrderResponseBody;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aliyun.repo.AliyunCertRepo;
import com.baiyi.cratos.eds.core.BaseEdsInstanceProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/2/26 10:31
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.ALIYUN, assetType = EdsAssetTypeEnum.ALIYUN_CERT)
public class AliyunCertProvider extends BaseEdsInstanceProvider<EdsAliyunConfigModel.Aliyun, ListUserCertificateOrderResponseBody.ListUserCertificateOrderResponseBodyCertificateOrderList> {

    private final AliyunCertRepo aliyunCertRepo;

    @Override
    protected List<ListUserCertificateOrderResponseBody.ListUserCertificateOrderResponseBodyCertificateOrderList> listEntities(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance) throws EdsQueryEntitiesException {
        try {
            return aliyunCertRepo.listUserCertOrder(instance.getEdsConfigModel());
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance, ListUserCertificateOrderResponseBody.ListUserCertificateOrderResponseBodyCertificateOrderList entity) throws ParseException {
        // https://help.aliyun.com/zh/ssl-certificate/developer-reference/api-cas-2020-04-07-listusercertificateorder?spm=a2c4g.11186623.0.0.7c9d4c27ACYKGJ
        return newEdsAssetBuilder(instance, entity)
                // 资源 ID
                .assetIdOf(entity.getInstanceId())
                .nameOf(entity.getDomain())
                .kindOf(entity.getCertType())
                .statusOf(entity.getStatus())
                .descriptionOf(entity.getSans())
                .createdTimeOf(entity.getCertStartTime())
                .expiredTimeOf(entity.getCertEndTime())
                .build();
    }

}
