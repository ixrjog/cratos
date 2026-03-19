package com.baiyi.cratos.eds.dingtalk.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.BaseEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.context.EdsAssetProviderContext;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkRobotModel;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/8 上午11:10
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.DINGTALK_ROBOT, assetTypeOf = EdsAssetTypeEnum.DINGTALK_ROBOT_MSG)
public class EdsDingtalkRobotMsgAssetProvider extends BaseEdsAssetProvider<EdsConfigs.Robot, DingtalkRobotModel.Msg> {

    public EdsDingtalkRobotMsgAssetProvider(EdsAssetProviderContext context) {
        super(context);
    }

    @Override
    protected List<DingtalkRobotModel.Msg> listEntities(
            ExternalDataSourceInstance<EdsConfigs.Robot> instance) throws EdsQueryEntitiesException {
        throw new EdsQueryEntitiesException("Unsupported.");
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Robot> instance,
                                  DingtalkRobotModel.Msg entity) {
        return newEdsAssetBuilder(instance, entity).build();
    }

}