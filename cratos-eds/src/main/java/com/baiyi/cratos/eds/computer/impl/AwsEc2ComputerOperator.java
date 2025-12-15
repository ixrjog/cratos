package com.baiyi.cratos.eds.computer.impl;

import com.baiyi.cratos.common.exception.CloudComputerOperationException;
import com.baiyi.cratos.eds.aws.repo.AwsEc2Repo;
import com.baiyi.cratos.eds.computer.BaseCloudComputerOperator;
import com.baiyi.cratos.eds.computer.context.CloudComputerContext;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.model.EdsAwsConfigModel;
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
 * &#064;Date  2025/11/19 17:03
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AWS, assetTypeOf = EdsAssetTypeEnum.AWS_EC2)
public class AwsEc2ComputerOperator extends BaseCloudComputerOperator<EdsAwsConfigModel.Aws, com.amazonaws.services.ec2.model.Instance> {

    private final AwsEc2Repo awsEc2Repo;

    public AwsEc2ComputerOperator(EdsInstanceService edsInstanceService, EdsConfigService edsConfigService,
                                  EdsAssetService edsAssetService, CredentialService credentialService,
                                  ConfigCredTemplate configCredTemplate, AwsEc2Repo awsEc2Repo) {
        super(edsInstanceService, edsConfigService, edsAssetService, credentialService, configCredTemplate);
        this.awsEc2Repo = awsEc2Repo;
    }

    /**
     * https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_RebootInstances.html
     *
     * @param context
     * @return
     * @throws CloudComputerOperationException
     */
    @Override
    protected String rebootInstance(
            CloudComputerContext<EdsAwsConfigModel.Aws> context) throws CloudComputerOperationException {
        return awsEc2Repo.rebootInstance(context.getRegionId(), context.getConfig(), context.getComputerInstanceId());
    }

    /**
     * https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_StartInstances.html
     *
     * @param context
     * @return
     * @throws CloudComputerOperationException
     */
    @Override
    protected String startInstance(
            CloudComputerContext<EdsAwsConfigModel.Aws> context) throws CloudComputerOperationException {
        return awsEc2Repo.startInstance(context.getRegionId(), context.getConfig(), context.getComputerInstanceId());
    }

    /**
     * https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_StopInstances.html
     *
     * @param context
     * @return
     * @throws CloudComputerOperationException
     */
    @Override
    protected String stopInstance(
            CloudComputerContext<EdsAwsConfigModel.Aws> context) throws CloudComputerOperationException {
        return awsEc2Repo.stopInstance(context.getRegionId(), context.getConfig(), context.getComputerInstanceId());
    }

}
