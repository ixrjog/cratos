package com.baiyi.cratos.eds.googlecloud.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.BaseMultipleSourcesEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsGcpConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsAssetConversionException;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.googlecloud.model.GoogleCertificateModel;
import com.baiyi.cratos.eds.googlecloud.repo.GcpCredentialRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
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
public class EdsGcpCertificateAssetProvider extends BaseMultipleSourcesEdsAssetProvider<EdsGcpConfigModel.Gcp, GoogleCertificateModel.Certificate> {

    private final GcpCredentialRepo googleCloudCredentialRepo;

    public EdsGcpCertificateAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                          CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                          EdsAssetIndexFacade edsAssetIndexFacade,
                                          UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                          EdsInstanceProviderHolderBuilder holderBuilder,
                                          GcpCredentialRepo googleCloudCredentialRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
        this.googleCloudCredentialRepo = googleCloudCredentialRepo;
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsGcpConfigModel.Gcp> instance,
                                  GoogleCertificateModel.Certificate entity) throws EdsAssetConversionException {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getKey())
                .assetKeyOf(entity.getKey())
                .nameOf(entity.getName())
                .createdTimeOf(entity.getCreatedTime())
                .expiredTimeOf(entity.getExpiredTime())
                .descriptionOf(entity.getDescription())
                .build();
    }

    @Override
    protected Set<String> getSources(
            ExternalDataSourceInstance<EdsGcpConfigModel.Gcp> instance) throws EdsQueryEntitiesException {
        try {
            return Sets.newHashSet(Optional.of(instance)
                    .map(ExternalDataSourceInstance::getEdsConfigModel)
                    .map(EdsGcpConfigModel.Gcp::getCertificate)
                    .map(EdsGcpConfigModel.Certificate::getLocations)
                    .orElse(List.of()));
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected List<GoogleCertificateModel.Certificate> listEntities(String namespace,
                                                                    ExternalDataSourceInstance<EdsGcpConfigModel.Gcp> instance) throws EdsQueryEntitiesException {
        try {
            return googleCloudCredentialRepo.listCertificates(namespace, instance.getEdsConfigModel())
                    .stream()
                    .map(GoogleCertificateModel::toCertificate)
                    .toList();
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

}