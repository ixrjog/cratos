package com.baiyi.cratos.eds.aws.repo.iam;

import com.amazonaws.services.identitymanagement.model.*;
import com.baiyi.cratos.eds.aws.service.AmazonIdentityManagementService;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public void deactivateMFADevice(EdsAwsConfigModel.Aws aws, String serialNumber, String iamUsername) {
        DeactivateMFADeviceRequest request = new DeactivateMFADeviceRequest();
        request.setUserName(iamUsername);
        request.setSerialNumber(serialNumber);
        DeactivateMFADeviceResult result = AmazonIdentityManagementService.buildAmazonIdentityManagement(aws)
                .deactivateMFADevice(request);
    }

    public void deleteVirtualMFADevice(EdsAwsConfigModel.Aws aws, String serialNumber) {
        DeleteVirtualMFADeviceRequest request = new DeleteVirtualMFADeviceRequest();
        request.setSerialNumber(serialNumber);
        DeleteVirtualMFADeviceResult result = AmazonIdentityManagementService.buildAmazonIdentityManagement(aws)
                .deleteVirtualMFADevice(request);
    }

    public List<MFADevice> listMFADevices(EdsAwsConfigModel.Aws aws, String iamUsername) {
        ListMFADevicesRequest request = new ListMFADevicesRequest();
        request.setUserName(iamUsername);
        ListMFADevicesResult result = AmazonIdentityManagementService.buildAmazonIdentityManagement(aws)
                .listMFADevices(request);
        return result.getMFADevices();
    }

}
