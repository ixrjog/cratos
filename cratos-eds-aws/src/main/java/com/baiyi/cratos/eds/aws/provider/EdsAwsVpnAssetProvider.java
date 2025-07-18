package com.baiyi.cratos.eds.aws.provider;

import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.ec2.model.VpnConnection;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.BaseHasRegionsEdsAssetProvider;
import com.baiyi.cratos.eds.aws.repo.AwsVpnRepo;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
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

/**
 * @Author baiyi
 * @Date 2024/3/6 10:26
 * @Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AWS, assetTypeOf = EdsAssetTypeEnum.AWS_STS_VPN)
public class EdsAwsVpnAssetProvider extends BaseHasRegionsEdsAssetProvider<EdsAwsConfigModel.Aws, VpnConnection> {

    public EdsAwsVpnAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                  CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                  EdsAssetIndexFacade edsAssetIndexFacade,
                                  UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                  EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
    }

    @Override
    protected List<VpnConnection> listEntities(String regionId, EdsAwsConfigModel.Aws aws) {
        return AwsVpnRepo.describeVpnConnections(regionId, aws);
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance, VpnConnection entity) {
        // https://docs.aws.amazon.com/acm/latest/APIReference/API_ListCertificates.html
        Optional<Tag> optionalTag = entity.getTags()
                .stream()
                .filter(e -> "Name".equals(e.getKey()))
                .findAny();
        final String name = optionalTag.isPresent() ? optionalTag.get()
                .getValue() : entity.getVpnConnectionId();
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getVpnConnectionId())
                .nameOf(name)
                .kindOf(entity.getType())
                .statusOf(entity.getState())
                .build();
    }

}
