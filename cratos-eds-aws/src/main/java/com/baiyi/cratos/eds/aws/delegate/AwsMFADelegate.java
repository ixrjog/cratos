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
 * @Author baiyi
 * @Date 2025/12/4 10:02
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AwsMFADelegate {

    private final AwsMFADeviceRepo awsMFADeviceRepo;

    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    @Retryable(retryFor = RetryException.class, maxAttempts = 5, backoff = @Backoff(delay = 3000))
    public void enableMFADevice(EdsAwsConfigModel.Aws aws, String iamUsername,
                                VirtualMFADevice vMFADevice) throws RetryException {
        try {
            TimeUnit.SECONDS.sleep(15L);
            log.info(
                    "Attempting to enable IAM virtual MFA: IAM username={}, serialNumber={}", iamUsername,
                    vMFADevice.getSerialNumber()
            );
            String secretKey = new String(vMFADevice.getBase32StringSeed()
                                                  .array());
            SecretKey key = OptGenerator.of(secretKey);
            OTPAccessCode.AccessCode accessCode = OptGenerator.generateOtpAccessCode(key);
            EnableMFADeviceResult result = awsMFADeviceRepo.enableMFADevice(
                    aws, iamUsername, vMFADevice.getSerialNumber(), accessCode.getCurrentPassword(),
                    accessCode.getFuturePassword()
            );
            log.info(
                    "Successfully enabled virtual MFA device: IAM username={}, requestId={}", iamUsername,
                    result.getSdkResponseMetadata()
                            .getRequestId()
            );
        } catch (Exception e) {
            log.warn("Failed to enable virtual MFA device: {}", e.getMessage());
            throw new RetryException(e.getMessage());
        }
    }

}
