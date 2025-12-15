package com.baiyi.cratos.eds.aws.device;

import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.model.*;
import com.baiyi.cratos.eds.aws.service.AmazonIdentityManagementService;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/3 15:39
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class IamVirtualMfa {

    /**
     * https://docs.aws.amazon.com/IAM/latest/APIReference/API_CreateVirtualMFADevice.html
     * 创建虚拟MFA设备
     *
     * @param aws
     * @param user
     * @return
     */
    @Retryable(retryFor = RetryException.class, maxAttempts = 2, backoff = @Backoff(delay = 5000))
    public VirtualMFADevice createVirtualMFADevice(EdsConfigs.Aws aws, com.baiyi.cratos.domain.generator.User user) throws RetryException {
        try {
            CreateVirtualMFADeviceRequest request = new CreateVirtualMFADeviceRequest();
            request.setVirtualMFADeviceName(user.getUsername());
            CreateVirtualMFADeviceResult result = buildAmazonIdentityManagement(aws).createVirtualMFADevice(request);
            return result.getVirtualMFADevice();
        } catch (Exception e) {
            log.error("创建虚拟MFA设备错误: {}", e.getMessage());
            throw new RetryException("创建虚拟MFA设备错误: " + e.getMessage());
        }
    }

    /**
     * 启用MFA设备(与用户绑定)
     *
     * @param aws
     * @param user
     * @param serialNumber arn:aws:iam::123456789012:mfa/ExampleName
     */
    public EnableMFADeviceResult enableMFADevice(EdsConfigs.Aws aws, com.baiyi.cratos.domain.generator.User user, String serialNumber, String authenticationCode1, String authenticationCode2) {
        EnableMFADeviceRequest request = new EnableMFADeviceRequest();
        request.setUserName(user.getUsername());
        request.setSerialNumber(serialNumber);
        request.setAuthenticationCode1(authenticationCode1);
        request.setAuthenticationCode2(authenticationCode2);
        return buildAmazonIdentityManagement(aws).enableMFADevice(request);
    }

    /**
     * 删除MFA设备
     *
     * @param aws
     * @param serialNumber arn:aws:iam::123456789012:mfa/ExampleName
     */
    public void deleteVirtualMFADevice(EdsConfigs.Aws aws, String serialNumber) {
        DeleteVirtualMFADeviceRequest request = new DeleteVirtualMFADeviceRequest();
        request.setSerialNumber(serialNumber);
        buildAmazonIdentityManagement(aws).deleteVirtualMFADevice(request);
    }

    private AmazonIdentityManagement buildAmazonIdentityManagement(EdsConfigs.Aws aws) {
        return AmazonIdentityManagementService.buildAmazonIdentityManagement(aws);
    }

}
