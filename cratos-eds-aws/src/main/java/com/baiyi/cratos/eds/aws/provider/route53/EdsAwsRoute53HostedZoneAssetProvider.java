package com.baiyi.cratos.eds.aws.provider.route53;

import com.amazonaws.services.route53.model.HostedZone;
import com.amazonaws.services.route53.model.HostedZoneConfig;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.aws.repo.AwsRoute53Repo;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.comparer.EdsAssetComparer;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.DOMAIN_NAME;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/12 14:55
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AWS, assetTypeOf = EdsAssetTypeEnum.AWS_HOSTED_ZONE)
public class EdsAwsRoute53HostedZoneAssetProvider extends BaseEdsInstanceAssetProvider<EdsAwsConfigModel.Aws, HostedZone> {

    public EdsAwsRoute53HostedZoneAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                                CredentialService credentialService,
                                                ConfigCredTemplate configCredTemplate,
                                                EdsAssetIndexFacade edsAssetIndexFacade,
                                                UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                                EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
    }

    @Override
    protected List<HostedZone> listEntities(
            ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance) throws EdsQueryEntitiesException {
        EdsAwsConfigModel.Aws aws = instance.getEdsConfigModel();
        try {
            return AwsRoute53Repo.listHostedZones(aws);
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance, HostedZone entity) {
        // https://docs.aws.amazon.com/Route53/latest/APIReference/API_domains_ListDomains.html
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getId())
                .nameOf(entity.getName())
                .descriptionOf(Optional.of(entity)
                        .map(HostedZone::getConfig)
                        .map(HostedZoneConfig::getComment)
                        .orElse(null))
                .build();
    }

    @Override
    protected boolean equals(EdsAsset a1, EdsAsset a2) {
        return EdsAssetComparer.DIFFERENT;
    }

    @Override
    protected EdsAssetIndex toEdsAssetIndex(ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance,
                                            EdsAsset edsAsset, HostedZone entity) {
        String name = edsAsset.getName();
        String domainName = name != null && name.endsWith(".") ? org.apache.commons.lang3.StringUtils.removeEnd(name,
                ".") : name;
        return toEdsAssetIndex(edsAsset, DOMAIN_NAME, domainName);
    }

}