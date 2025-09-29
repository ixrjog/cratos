package com.baiyi.cratos.eds.eaglecloud.sase.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.param.http.event.EagleCloudEventParam;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsEagleCloudConfigModel;
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

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/29 15:54
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.EAGLECLOUD_SASE, assetTypeOf = EdsAssetTypeEnum.EAGLECLOUD_SASE_DATA_SECURITY_EVENT)
public class EdsEagleCloudSaseHookAssetProvider extends BaseEdsInstanceAssetProvider<EdsEagleCloudConfigModel.Sase, EagleCloudEventParam.SaseHook> {

    public EdsEagleCloudSaseHookAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                              CredentialService credentialService,
                                              ConfigCredTemplate configCredTemplate,
                                              EdsAssetIndexFacade edsAssetIndexFacade,
                                              UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                              EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
    }

    @Override
    protected List<EagleCloudEventParam.SaseHook> listEntities(
            ExternalDataSourceInstance<EdsEagleCloudConfigModel.Sase> instance) throws EdsQueryEntitiesException {
        throw new EdsQueryEntitiesException("Query not supported.");
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsEagleCloudConfigModel.Sase> instance,
                                         EagleCloudEventParam.SaseHook entity) {
        EagleCloudEventParam.Content content = EagleCloudEventParam.Content.parse(entity);
        return newEdsAssetBuilder(instance, entity).assetIdOf(content.getEventId())
                .nameOf(content.getEntityName())
                .kindOf(content.getEntityName())
                .descriptionOf(content.getThreshold())
                .build();
    }

//    @Override
//    protected List<EdsAssetIndex> convertToEdsAssetIndexList(
//            ExternalDataSourceInstance<EdsEagleCloudConfigModel.Sase> instance, EdsAsset edsAsset,
//            EagleCloudEventParam.SaseHook entity) {
//        List<EdsAssetIndex> indices = Lists.newArrayList();
////        Optional.ofNullable(entity.getUserId())
////                .filter(IdentityUtils::hasIdentity)
////                .ifPresent(userId -> indices.add(createEdsAssetIndex(edsAsset, GITLAB_USER_ID, userId)));
////        Optional.ofNullable(entity.getProjectId())
////                .filter(IdentityUtils::hasIdentity)
////                .ifPresent(projectId -> indices.add(createEdsAssetIndex(edsAsset, GITLAB_PROJECT_ID, projectId)));
////        Optional.ofNullable(entity.getGroupId())
////                .filter(IdentityUtils::hasIdentity)
////                .ifPresent(projectId -> indices.add(createEdsAssetIndex(edsAsset, GITLAB_GROUP_ID, projectId)));
//        return indices;
//    }

}