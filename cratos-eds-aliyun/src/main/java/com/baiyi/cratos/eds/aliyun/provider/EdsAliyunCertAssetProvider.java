package com.baiyi.cratos.eds.aliyun.provider;

import com.aliyun.cas20200407.models.ListCertificatesResponseBody;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aliyun.repo.AliyunCertRepo;
import com.baiyi.cratos.eds.core.BaseEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.context.EdsAssetProviderContext;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/2/26 10:31
 * &#064;Version  1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_CERT)
public class EdsAliyunCertAssetProvider extends BaseEdsAssetProvider<EdsConfigs.Aliyun, ListCertificatesResponseBody.ListCertificatesResponseBodyCertificateList> {

    private final AliyunCertRepo aliyunCertRepo;

    public EdsAliyunCertAssetProvider(EdsAssetProviderContext context, AliyunCertRepo aliyunCertRepo) {
        super(context);
        this.aliyunCertRepo = aliyunCertRepo;
    }

    @Override
    protected List<ListCertificatesResponseBody.ListCertificatesResponseBodyCertificateList> listEntities(
            ExternalDataSourceInstance<EdsConfigs.Aliyun> instance) throws EdsQueryEntitiesException {
        try {
            return aliyunCertRepo.listCertificates(instance.getConfig());
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    /**
     * https://help.aliyun.com/zh/ssl-certificate/developer-reference/api-cas-2020-04-07-listcertificates?spm=a2c4g.11186623.help-menu-28533.d_8_3_3_0_9.7ce7410dzUxqvV
     *
     * @param instance
     * @param entity
     * @return
     */
    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Aliyun> instance,
                                         ListCertificatesResponseBody.ListCertificatesResponseBodyCertificateList entity) {
        return createAssetBuilder(instance, entity)
                // 资源 ID
                .assetIdOf(entity.getCertificateId())
                .assetKeyOf(entity.getCertIdentifier())
                .nameOf(entity.getDomain())
                .kindOf(entity.getIssuer())
                .statusOf(entity.getCertificateStatus())
                .createdTimeOf(entity.getNotBefore())
                .expiredTimeOf(entity.getNotAfter())
                .descriptionOf(Optional.ofNullable(entity.getSubjectAlternativeNames())
                                       .filter(list -> !list.isEmpty())
                                       .map(list -> String.join(",", list))
                                       .orElse(null))
                .build();
    }

}
