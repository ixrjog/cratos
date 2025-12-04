package com.baiyi.cratos.eds.aws.delegate;

import com.amazonaws.services.identitymanagement.model.EnableMFADeviceResult;
import com.amazonaws.services.identitymanagement.model.VirtualMFADevice;
import com.baiyi.cratos.common.otp.OptGenerator;
import com.baiyi.cratos.common.otp.model.OTPAccessCode;
import com.baiyi.cratos.eds.aws.repo.iam.AwsMFADeviceRepo;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.concurrent.TimeUnit;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/4 10:02
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AwsMFADelegate {

    private final AwsMFADeviceRepo awsMFADeviceRepo;

    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    @Retryable(retryFor = RetryException.class, maxAttempts = 5, backoff = @Backoff(delay = 3000))
    public void enableMFADevice(EdsAwsConfigModel.Aws aws, String iamUsername, VirtualMFADevice vMFADevice) throws RetryException {
        try {
            TimeUnit.SECONDS.sleep(3L);
            log.info("尝试启用IAM虚拟MFA: username={}, serialNumber={}", iamUsername, vMFADevice.getSerialNumber());
            String secretKeyStr = new String(vMFADevice.getBase32StringSeed().array());
            SecretKey key = OptGenerator.of(secretKeyStr);
            OTPAccessCode.AccessCode accessCode = OptGenerator.generateOtpAccessCode(key);
            EnableMFADeviceResult result = awsMFADeviceRepo.enableMFADevice(aws, iamUsername, vMFADevice.getSerialNumber(), accessCode.getCurrentPassword(), accessCode.getFuturePassword());
            log.info("启用虚拟MFA设备成功: username={}, requestId={}", iamUsername, result.getSdkResponseMetadata().getRequestId());
        } catch (Exception e) {
            log.error("启用虚拟MFA设备失败: {}", e.getMessage());
            throw new RetryException(e.getMessage());
        }
    }

}
