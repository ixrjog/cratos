package com.baiyi.cratos.eds.aliyun.provider;

import com.aliyun.cas20200407.models.ListCertResponseBody;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aliyun.repo.AliyunCertRepo;
import com.baiyi.cratos.eds.core.BaseEdsInstanceProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/2/26 10:31
 * @Version 1.0
 */
@Component
@AllArgsConstructor
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.ALIYUN, assetType = EdsAssetTypeEnum.ALIYUN_CERT)
public class AliyunCertProvider extends BaseEdsInstanceProvider<EdsAliyunConfigModel.Aliyun, ListCertResponseBody.ListCertResponseBodyCertList> {

    private final AliyunCertRepo aliyunCertRepo;

    @Override
    protected List<ListCertResponseBody.ListCertResponseBodyCertList> listEntities(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance) throws EdsQueryEntitiesException {
        try {
            return aliyunCertRepo.listCert(instance.getEdsConfigModel());
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance, ListCertResponseBody.ListCertResponseBodyCertList entity) {
        // https://help.aliyun.com/zh/ssl-certificate/developer-reference/api-cas-2020-04-07-listcert?spm=a2c4g.11174283.0.0.4b484c27CkzXih
        return newEdsAssetBuilder(instance, entity)
                .assetIdOf(entity.getIdentifier())
                .nameOf(entity.getCommonName())
                .kindOf(entity.getCertType())
                .statusOf(entity.getStatus())
                .createdTimeOf(entity.getBeforeDate())
                .expiredTimeOf(entity.getAfterDate())
                .build();
    }

}