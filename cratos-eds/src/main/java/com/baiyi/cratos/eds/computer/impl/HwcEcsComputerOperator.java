package com.baiyi.cratos.eds.computer.impl;

import com.baiyi.cratos.common.exception.CloudComputerOperationException;
import com.baiyi.cratos.eds.computer.BaseCloudComputerOperator;
import com.baiyi.cratos.eds.computer.context.CloudComputerContext;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsHwcConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.huaweicloud.cloud.repo.HwcEcsRepo;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.ecs.v2.model.ServerDetail;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/19 17:23
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.HUAWEICLOUD, assetTypeOf = EdsAssetTypeEnum.HUAWEICLOUD_ECS)
public class HwcEcsComputerOperator extends BaseCloudComputerOperator<EdsHwcConfigModel.Hwc, ServerDetail> {

    public HwcEcsComputerOperator(EdsInstanceService edsInstanceService, EdsConfigService edsConfigService,
                                  EdsAssetService edsAssetService, CredentialService credentialService,
                                  ConfigCredTemplate configCredTemplate) {
        super(edsInstanceService, edsConfigService, edsAssetService, credentialService, configCredTemplate);
    }

    /**
     * https://support.huaweicloud.com/intl/zh-cn/api-ecs/ecs_02_0302.html
     *
     * @param context
     * @return
     * @throws CloudComputerOperationException
     */
    @Override
    protected String rebootInstance(
            CloudComputerContext<EdsHwcConfigModel.Hwc> context) throws CloudComputerOperationException {
        try {
            return HwcEcsRepo.rebootInstance(
                    context.getRegionId(), context.getConfig(), context.getComputerInstanceId());
        } catch (ServiceResponseException serviceResponseException) {
            throw new CloudComputerOperationException(serviceResponseException.getMessage());
        }
    }

    /**
     * https://support.huaweicloud.com/intl/zh-cn/api-ecs/ecs_02_0301.html
     *
     * @param context
     * @return
     * @throws CloudComputerOperationException
     */
    @Override
    protected String startInstance(
            CloudComputerContext<EdsHwcConfigModel.Hwc> context) throws CloudComputerOperationException {
        try {
            return HwcEcsRepo.startInstance(
                    context.getRegionId(), context.getConfig(), context.getComputerInstanceId());
        } catch (ServiceResponseException serviceResponseException) {
            throw new CloudComputerOperationException(serviceResponseException.getMessage());
        }
    }

    /**
     * https://support.huaweicloud.com/intl/zh-cn/api-ecs/ecs_02_0303.html
     *
     * @param context
     * @return
     * @throws CloudComputerOperationException
     */
    @Override
    protected String stopInstance(
            CloudComputerContext<EdsHwcConfigModel.Hwc> context) throws CloudComputerOperationException {
        try {
            return HwcEcsRepo.stopInstance(context.getRegionId(), context.getConfig(), context.getComputerInstanceId());
        } catch (ServiceResponseException serviceResponseException) {
            throw new CloudComputerOperationException(serviceResponseException.getMessage());
        }
    }

}

