package com.baiyi.cratos.eds.sre.bridge.provider;

import com.baiyi.cratos.common.util.PasswordGenerator;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.model.SreBridgeModel;
import com.baiyi.cratos.eds.core.AssetToBusinessObjectUpdater;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsProviderHolderFactory;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.sre.bridge.facade.SreBridgeFacade;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.baiyi.cratos.eds.core.util.SreEventFormatter.EVENT_ID;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/9 16:08
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.SRE_EVENTBRIDGE, assetTypeOf = EdsAssetTypeEnum.SRE_EVENTBRIDGE_EVENT)
public class EdsSreBridgeEventAssetProvider extends BaseEdsInstanceAssetProvider<EdsConfigs.SreEventBridge, SreBridgeModel.Event> {

    public EdsSreBridgeEventAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                          CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                          EdsAssetIndexFacade edsAssetIndexFacade,
                                          AssetToBusinessObjectUpdater assetToBusinessObjectUpdater,
                                          EdsProviderHolderFactory holderBuilder) {
        super(
                edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                assetToBusinessObjectUpdater, holderBuilder
        );
    }

    @Override
    protected List<SreBridgeModel.Event> listEntities(
            ExternalDataSourceInstance<EdsConfigs.SreEventBridge> instance) throws EdsQueryEntitiesException {
        throw new EdsQueryEntitiesException("不支持");
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.SreEventBridge> instance,
                                         SreBridgeModel.Event entity) {
        Map<String, String> ext = Optional.of(entity)
                .map(SreBridgeModel.Event::getExt)
                .orElse(Map.of());
        String eventId = ext.containsKey(EVENT_ID) ? ext.get(EVENT_ID) : PasswordGenerator.generateNo();
        return newEdsAssetBuilder(instance, entity).assetIdOf(eventId)
                .nameOf(entity.getAction())
                .kindOf(entity.getType())
                .createdTimeOf(entity.getTime())
                .descriptionOf(entity.getDescription())
                .build();
    }

    @Override
    protected EdsAsset saveEntityAsAsset(ExternalDataSourceInstance<EdsConfigs.SreEventBridge> instance,
                                         SreBridgeModel.Event entity) {
        EdsAsset asset = super.saveEntityAsAsset(instance, entity);
        // 获取符合条件的标签资源
        SreBridgeFacade.collectEvent(instance.getConfig(), entity);
        return asset;
    }

}