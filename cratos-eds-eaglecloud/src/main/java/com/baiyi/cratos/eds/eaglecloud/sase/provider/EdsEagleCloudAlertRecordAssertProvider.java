package com.baiyi.cratos.eds.eaglecloud.sase.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.BaseEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.context.EdsAssetProviderContext;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.eaglecloud.sase.model.EagleCloudModel;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/9 10:10
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.EAGLECLOUD_SASE, assetTypeOf = EdsAssetTypeEnum.EAGLECLOUD_SASE_DATA_SECURITY_ALERT_RECORD)
public class EdsEagleCloudAlertRecordAssertProvider extends BaseEdsAssetProvider<EdsConfigs.Sase, EagleCloudModel.AlertRecord> {

    public EdsEagleCloudAlertRecordAssertProvider(EdsAssetProviderContext context) {
        super(context);
    }

    @Override
    protected List<EagleCloudModel.AlertRecord> listEntities(
            ExternalDataSourceInstance<EdsConfigs.Sase> instance) throws EdsQueryEntitiesException {
        throw new EdsQueryEntitiesException("Query not supported.");
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Sase> instance,
                                         EagleCloudModel.AlertRecord entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getEventId())
                .nameOf(entity.getName())
                .descriptionOf(entity.getDescription())
                .build();
    }

}