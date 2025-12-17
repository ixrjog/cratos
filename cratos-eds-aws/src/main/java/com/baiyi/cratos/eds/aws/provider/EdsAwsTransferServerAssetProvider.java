package com.baiyi.cratos.eds.aws.provider;

import com.amazonaws.services.transfer.model.ListedServer;
import com.amazonaws.services.transfer.model.ListedUser;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aws.model.AwsTransferServer;
import com.baiyi.cratos.eds.aws.repo.AwsTransferRepo;
import com.baiyi.cratos.eds.core.AssetToBusinessObjectUpdater;
import com.baiyi.cratos.eds.core.BaseHasRegionsEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.comparer.EdsAssetComparer;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/13 下午2:02
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AWS, assetTypeOf = EdsAssetTypeEnum.AWS_TRANSFER_SERVER)
public class EdsAwsTransferServerAssetProvider extends BaseHasRegionsEdsAssetProvider<EdsConfigs.Aws, AwsTransferServer.TransferServer> {

    public EdsAwsTransferServerAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                             CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                             EdsAssetIndexFacade edsAssetIndexFacade,
                                             AssetToBusinessObjectUpdater assetToBusinessObjectUpdater,
                                             EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                assetToBusinessObjectUpdater, holderBuilder);
    }

    @Override
    protected List<AwsTransferServer.TransferServer> listEntities(String regionId, EdsConfigs.Aws aws) {
        List<ListedServer> servers = AwsTransferRepo.listServers(regionId, aws);
        if (!CollectionUtils.isEmpty(servers)) {
            return toTransferServer(regionId, aws, servers);
        }
        return Collections.emptyList();
    }

    private List<AwsTransferServer.TransferServer> toTransferServer(String regionId, EdsConfigs.Aws aws,
                                                                    List<ListedServer> servers) {
        return servers.stream()
                .map(e -> {
                    List<ListedUser> users = AwsTransferRepo.listUsers(regionId, aws, e.getServerId());
                    return AwsTransferServer.TransferServer.builder()
                            .regionId(regionId)
                            .server(e)
                            .users(users)
                            .build();
                })
                .toList();
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Aws> instance,
                                  AwsTransferServer.TransferServer entity) {
        return newEdsAssetBuilder(instance, entity)
                .assetIdOf(entity.getServer()
                        .getServerId())
                .nameOf(entity.getServer()
                        .getServerId())
                .assetKeyOf(entity.getServer()
                        .getArn())
                .regionOf(entity.getRegionId())
                .build();
    }

    @Override
    protected boolean equals(EdsAsset a1, EdsAsset a2) {
        return EdsAssetComparer.DIFFERENT;
    }

}
