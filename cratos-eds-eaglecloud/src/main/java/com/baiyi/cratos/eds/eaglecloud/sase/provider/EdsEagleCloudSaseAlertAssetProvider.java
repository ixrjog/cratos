package com.baiyi.cratos.eds.eaglecloud.sase.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.param.http.event.EagleCloudEventParam;
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

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/30 10:53
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.EAGLECLOUD_SASE, assetTypeOf = EdsAssetTypeEnum.EAGLECLOUD_SASE_DATA_SECURITY_ALERT_NOTIFICATION)
public class EdsEagleCloudSaseAlertAssetProvider extends BaseEdsAssetProvider<EdsConfigs.Sase, EagleCloudEventParam.Alert> {

    public EdsEagleCloudSaseAlertAssetProvider(EdsAssetProviderContext context) {
        super(context);
    }

    @Override
    protected List<EagleCloudEventParam.Alert> listEntities(
            ExternalDataSourceInstance<EdsConfigs.Sase> instance) throws EdsQueryEntitiesException {
        throw new EdsQueryEntitiesException("Query not supported.");
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Sase> instance,
                                         EagleCloudEventParam.Alert entity) {
        EagleCloudEventParam.Content content = entity.getContent();
        return newEdsAssetBuilder(instance, entity).assetIdOf(content.getEventId())
                .nameOf(content.getEntityName())
                .kindOf(content.getEntityName())
                .descriptionOf(content.getThreshold())
                .build();
    }

}