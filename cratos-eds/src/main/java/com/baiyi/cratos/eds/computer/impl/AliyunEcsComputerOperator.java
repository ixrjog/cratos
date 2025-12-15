package com.baiyi.cratos.eds.computer.impl;

import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.baiyi.cratos.common.exception.CloudComputerOperationException;
import com.baiyi.cratos.eds.aliyun.repo.AliyunEcsRepo;
import com.baiyi.cratos.eds.computer.BaseCloudComputerOperator;
import com.baiyi.cratos.eds.computer.context.CloudComputerContext;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.model.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.service.EdsInstanceService;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/19 11:13
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_ECS)
public class AliyunEcsComputerOperator extends BaseCloudComputerOperator<EdsAliyunConfigModel.Aliyun, DescribeInstancesResponse.Instance> {

    private final AliyunEcsRepo aliyunEcsRepo;

    public AliyunEcsComputerOperator(EdsInstanceService edsInstanceService, EdsConfigService edsConfigService,
                                     EdsAssetService edsAssetService, CredentialService credentialService,
                                     ConfigCredTemplate configCredTemplate, AliyunEcsRepo aliyunEcsRepo) {
        super(edsInstanceService, edsConfigService, edsAssetService, credentialService, configCredTemplate);
        this.aliyunEcsRepo = aliyunEcsRepo;
    }

    /**
     * https://help.aliyun.com/zh/ecs/developer-reference/api-ecs-2014-05-26-rebootinstance?spm=a2c4g.11186623.help-menu-25365.d_1_0_4_2_6.2fc427d42bNbx3&scm=20140722.H_2679683._.OR_help-T_cn~zh-V_1
     *
     * @param context
     * @return
     * @throws CloudComputerOperationException
     */
    @Override
    protected String rebootInstance(
            CloudComputerContext<EdsAliyunConfigModel.Aliyun> context) throws CloudComputerOperationException {
        return aliyunEcsRepo.rebootInstance(
                context.getRegionId(), context.getConfig(), context.getComputerInstanceId());
    }

    /**
     * https://help.aliyun.com/zh/ecs/developer-reference/api-ecs-2014-05-26-startinstance?spm=a2c4g.11186623.help-menu-25365.d_1_0_4_2_2.473376187f1CDu&scm=20140722.H_2679679._.OR_help-T_cn~zh-V_1
     *
     * @param context
     * @return
     * @throws CloudComputerOperationException
     */
    @Override
    protected String startInstance(
            CloudComputerContext<EdsAliyunConfigModel.Aliyun> context) throws CloudComputerOperationException {
        return aliyunEcsRepo.startInstance(
                context.getRegionId(), context.getConfig(), context.getComputerInstanceId());
    }

    /**
     * https://help.aliyun.com/zh/ecs/developer-reference/api-ecs-2014-05-26-stopinstance?spm=a2c4g.11186623.help-menu-25365.d_1_0_4_2_4.1bb174d8jumaW0&scm=20140722.H_2679681._.OR_help-T_cn~zh-V_1
     *
     * @param context
     * @return
     * @throws CloudComputerOperationException
     */
    @Override
    protected String stopInstance(
            CloudComputerContext<EdsAliyunConfigModel.Aliyun> context) throws CloudComputerOperationException {
        return aliyunEcsRepo.stopInstance(
                context.getRegionId(), context.getConfig(), context.getComputerInstanceId());
    }

}
