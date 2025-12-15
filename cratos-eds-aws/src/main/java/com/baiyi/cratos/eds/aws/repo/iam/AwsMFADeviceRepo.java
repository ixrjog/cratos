package com.baiyi.cratos.eds.aws.repo.iam;

import com.amazonaws.services.identitymanagement.model.*;
import com.baiyi.cratos.eds.aws.service.AmazonIdentityManagementService;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/31 11:25
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AwsMFADeviceRepo {

    public static final String SERIAL_NUMBER_TPL = "arn:aws:iam::{}:mfa/{}";

    @Retryable(retryFor = RetryException.class, maxAttempts = 2, backoff = @Backoff(delay = 5000))
    public VirtualMFADevice createVirtualMFADevice(EdsConfigs.Aws aws,
                                                   String iamUsername) throws RetryException {
        try {
            CreateVirtualMFADeviceRequest request = new CreateVirtualMFADeviceRequest();
            request.setVirtualMFADeviceName(iamUsername);
            CreateVirtualMFADeviceResult result = AmazonIdentityManagementService.buildAmazonIdentityManagement(aws)
                    .createVirtualMFADevice(request);
            return result.getVirtualMFADevice();
        } catch (Exception e) {
            log.error("创建虚拟MFA设备错误: {}", e.getMessage());
            throw new RetryException("创建虚拟MFA设备错误: " + e.getMessage());
        }
    }

    public void deactivateMFADevice(EdsConfigs.Aws aws, String serialNumber, String iamUsername) {
        DeactivateMFADeviceRequest request = new DeactivateMFADeviceRequest();
        request.setUserName(iamUsername);
        request.setSerialNumber(serialNumber);
        DeactivateMFADeviceResult result = AmazonIdentityManagementService.buildAmazonIdentityManagement(aws)
                .deactivateMFADevice(request);
    }

    public void deleteVirtualMFADevice(EdsConfigs.Aws aws, String serialNumber) {
        DeleteVirtualMFADeviceRequest request = new DeleteVirtualMFADeviceRequest();
        request.setSerialNumber(serialNumber);
        DeleteVirtualMFADeviceResult result = AmazonIdentityManagementService.buildAmazonIdentityManagement(aws)
                .deleteVirtualMFADevice(request);
    }

    public List<MFADevice> listMFADevices(EdsConfigs.Aws aws, String iamUsername) {
        ListMFADevicesRequest request = new ListMFADevicesRequest();
        request.setUserName(iamUsername);
        ListMFADevicesResult result = AmazonIdentityManagementService.buildAmazonIdentityManagement(aws)
                .listMFADevices(request);
        return result.getMFADevices();
    }

    public EnableMFADeviceResult enableMFADevice(EdsConfigs.Aws aws, String iamUsername, String serialNumber, String authenticationCode1, String authenticationCode2) {
        EnableMFADeviceRequest request = new EnableMFADeviceRequest();
        request.setUserName(iamUsername);
        request.setSerialNumber(serialNumber);
        request.setAuthenticationCode1(authenticationCode1);
        request.setAuthenticationCode2(authenticationCode2);
        return AmazonIdentityManagementService.buildAmazonIdentityManagement(aws).enableMFADevice(request);
    }

}
