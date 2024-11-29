package com.baiyi.cratos.eds.googlecloud.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.comparer.EdsAssetComparer;
import com.baiyi.cratos.eds.core.config.EdsGoogleCloudConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsAssetConversionException;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
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

/**
 * @Author 修远
 * @Date 2024/7/30 上午11:16
 * @Since 1.0
 */

@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.GOOGLECLOUD, assetTypeOf = EdsAssetTypeEnum.GOOGLECLOUD_MEMBER)
public class EdsGcpMemberAssetProvider extends BaseEdsInstanceAssetProvider<EdsGoogleCloudConfigModel.GoogleCloud, GoogleMemberModel.Member> {

    private final GcpProjectRepo googleCloudProjectRepo;

    public EdsGcpMemberAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                     CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                     EdsAssetIndexFacade edsAssetIndexFacade,
                                     UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                     GcpProjectRepo googleCloudProjectRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler);
        this.googleCloudProjectRepo = googleCloudProjectRepo;
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsGoogleCloudConfigModel.GoogleCloud> instance,
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
            ExternalDataSourceInstance<EdsGoogleCloudConfigModel.GoogleCloud> instance) throws EdsQueryEntitiesException {
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
    protected List<EdsAssetIndex> toEdsAssetIndexList(
            ExternalDataSourceInstance<EdsGoogleCloudConfigModel.GoogleCloud> instance, EdsAsset edsAsset,
            GoogleMemberModel.Member entity) {
        // "roles/"
        String roles = Joiner.on(INDEX_VALUE_DIVISION_SYMBOL)
                .join(entity.getRoles()
                        .stream()
                        .map(role -> StringUtils.substring(role, 6))
                        .sorted()
                        .collect(Collectors.toList()));
        return Lists.newArrayList(toEdsAssetIndex(edsAsset, "roles", roles));
    }

    @Override
    protected boolean equals(EdsAsset a1, EdsAsset a2) {
        return EdsAssetComparer.DIFFERENT;
    }
}
