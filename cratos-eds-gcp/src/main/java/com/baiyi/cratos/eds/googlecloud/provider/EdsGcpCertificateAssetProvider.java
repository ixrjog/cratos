package com.baiyi.cratos.eds.googlecloud.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.BaseMultipleSourcesEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.config.model.EdsGcpConfigModel;
import com.baiyi.cratos.eds.core.context.EdsAssetProviderContext;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsAssetConversionException;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.googlecloud.model.GcpCertificateModel;
import com.baiyi.cratos.eds.googlecloud.repo.GcpCredentialRepo;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/29 下午3:23
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.GCP, assetTypeOf = EdsAssetTypeEnum.GCP_CERTIFICATE)
public class EdsGcpCertificateAssetProvider extends BaseMultipleSourcesEdsAssetProvider<EdsConfigs.Gcp, GcpCertificateModel.Certificate> {

    private final GcpCredentialRepo googleCloudCredentialRepo;

    public EdsGcpCertificateAssetProvider(EdsAssetProviderContext context,
                                          GcpCredentialRepo googleCloudCredentialRepo) {
        super(context);
        this.googleCloudCredentialRepo = googleCloudCredentialRepo;
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Gcp> instance,
                                  GcpCertificateModel.Certificate entity) throws EdsAssetConversionException {
        return createAssetBuilder(instance, entity).assetIdOf(entity.getKey())
                .assetKeyOf(entity.getKey())
                .nameOf(entity.getName())
                .createdTimeOf(entity.getCreatedTime())
                .expiredTimeOf(entity.getExpiredTime())
                .descriptionOf(entity.getDescription())
                .build();
    }

    @Override
    protected Set<String> getSources(
            ExternalDataSourceInstance<EdsConfigs.Gcp> instance) throws EdsQueryEntitiesException {
        try {
            return Sets.newHashSet(Optional.of(instance)
                    .map(ExternalDataSourceInstance::getConfig)
                    .map(EdsConfigs.Gcp::getCertificate)
                    .map(EdsGcpConfigModel.Certificate::getLocations)
                    .orElse(List.of()));
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected List<GcpCertificateModel.Certificate> listEntities(String namespace,
                                                                 ExternalDataSourceInstance<EdsConfigs.Gcp> instance) throws EdsQueryEntitiesException {
        try {
            return googleCloudCredentialRepo.listCertificates(namespace, instance.getConfig())
                    .stream()
                    .map(GcpCertificateModel::toCertificate)
                    .toList();
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

}