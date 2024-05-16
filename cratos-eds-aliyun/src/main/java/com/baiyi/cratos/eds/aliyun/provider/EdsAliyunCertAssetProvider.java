package com.baiyi.cratos.eds.aliyun.provider;

import com.aliyun.cas20200407.models.ListUserCertificateOrderResponseBody;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aliyun.repo.AliyunCertRepo;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/2/26 10:31
 * &#064;Version  1.0
 */
@Component
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.ALIYUN, assetType = EdsAssetTypeEnum.ALIYUN_CERT)
public class EdsAliyunCertAssetProvider extends BaseEdsInstanceAssetProvider<EdsAliyunConfigModel.Aliyun, ListUserCertificateOrderResponseBody.ListUserCertificateOrderResponseBodyCertificateOrderList> {

    private final AliyunCertRepo aliyunCertRepo;

    public EdsAliyunCertAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                      CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                      EdsAssetIndexFacade edsAssetIndexFacade, AliyunCertRepo aliyunCertRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade);
        this.aliyunCertRepo = aliyunCertRepo;
    }

    @Override
    protected List<ListUserCertificateOrderResponseBody.ListUserCertificateOrderResponseBodyCertificateOrderList> listEntities(
            ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance) throws EdsQueryEntitiesException {
        try {
            return aliyunCertRepo.listUserCertOrder(instance.getEdsConfigModel());
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
                                  ListUserCertificateOrderResponseBody.ListUserCertificateOrderResponseBodyCertificateOrderList entity) {
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
