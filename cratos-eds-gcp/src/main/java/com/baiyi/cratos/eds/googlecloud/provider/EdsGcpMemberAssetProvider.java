package com.baiyi.cratos.eds.googlecloud.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.comparer.EdsAssetComparer;
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
import com.baiyi.cratos.eds.googlecloud.model.GoogleMemberModel;
import com.baiyi.cratos.eds.googlecloud.repo.GcpProjectRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.CLOUD_ACCOUNT_USERNAME;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.GCP_MEMBER_ROLES;

/**
 * @Author 修远
 * @Date 2024/7/30 上午11:16
 * @Since 1.0
 */

@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.GCP, assetTypeOf = EdsAssetTypeEnum.GCP_MEMBER)
public class EdsGcpMemberAssetProvider extends BaseEdsInstanceAssetProvider<EdsGcpConfigModel.Gcp, GoogleMemberModel.Member> {

    private final GcpProjectRepo googleCloudProjectRepo;

    public EdsGcpMemberAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                     CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                     EdsAssetIndexFacade edsAssetIndexFacade,
                                     UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                     EdsInstanceProviderHolderBuilder holderBuilder,
                                     GcpProjectRepo googleCloudProjectRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
        this.googleCloudProjectRepo = googleCloudProjectRepo;
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsGcpConfigModel.Gcp> instance,
                                  GoogleMemberModel.Member entity) throws EdsAssetConversionException {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getName())
                .assetKeyOf(entity.getName())
                .nameOf(entity.getName())
                .kindOf(entity.getType())
                .descriptionOf(entity.getType())
                .build();
    }

    @Override
    protected List<GoogleMemberModel.Member> listEntities(
            ExternalDataSourceInstance<EdsGcpConfigModel.Gcp> instance) throws EdsQueryEntitiesException {
        try {
            return googleCloudProjectRepo.listMembers(instance.getEdsConfigModel())
                    .entrySet()
                    .stream()
                    .map(e -> GoogleMemberModel.toMember(e.getKey(), e.getValue()))
                    .toList();
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected List<EdsAssetIndex> convertToEdsAssetIndexList(
            ExternalDataSourceInstance<EdsGcpConfigModel.Gcp> instance, EdsAsset edsAsset,
            GoogleMemberModel.Member entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        // "roles/"
        String roles = Joiner.on(INDEX_VALUE_DIVISION_SYMBOL)
                .join(entity.getRoles()
                        .stream()
                        .map(role -> StringUtils.substring(role, 6))
                        .sorted()
                        .collect(Collectors.toList()));
        indices.add(createEdsAssetIndex(edsAsset, GCP_MEMBER_ROLES, roles));
        indices.add(createEdsAssetIndex(edsAsset, CLOUD_ACCOUNT_USERNAME, entity.getName()));
        return indices;
    }

    @Override
    protected boolean equals(EdsAsset a1, EdsAsset a2) {
        return EdsAssetComparer.DIFFERENT;
    }
}
