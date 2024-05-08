package com.baiyi.cratos.eds.dingtalk.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsDingtalkConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkRobot;
import com.baiyi.cratos.eds.dingtalk.repo.DingtalkDepartmentRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/8 上午11:10
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.DINGTALK_ROBOT, assetType = EdsAssetTypeEnum.DINGTALK_ROBOT_MSG)
public class EdsDingtalkRobotMsgAssetProvider extends BaseEdsInstanceAssetProvider<EdsDingtalkConfigModel.Robot, DingtalkRobot.Msg> {

    public EdsDingtalkRobotMsgAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                            CredentialService credentialService,
                                            ConfigCredTemplate configCredTemplate,
                                            EdsAssetIndexFacade edsAssetIndexFacade,
                                            DingtalkDepartmentRepo dingtalkDepartmentRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade);
    }

    @Override
    protected List<DingtalkRobot.Msg> listEntities(
            ExternalDataSourceInstance<EdsDingtalkConfigModel.Robot> instance) throws EdsQueryEntitiesException {
        throw new EdsQueryEntitiesException("Unsupported.");
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsDingtalkConfigModel.Robot> instance,
                                  DingtalkRobot.Msg entity) {
        return newEdsAssetBuilder(instance, entity)
                .build();
    }

}