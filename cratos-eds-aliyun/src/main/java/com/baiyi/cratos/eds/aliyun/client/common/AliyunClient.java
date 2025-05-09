package com.baiyi.cratos.eds.aliyun.client.common;

import com.aliyuncs.AcsRequest;
import com.aliyuncs.AcsResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/2/26 10:47
 * @Version 1.0
 */
@Component
public class AliyunClient {

    @Retryable(retryFor = ClientException.class, maxAttempts = 4, backoff = @Backoff(delay = 3000, multiplier = 1.5))
    public <T extends AcsResponse> T getAcsResponse(String regionId, EdsAliyunConfigModel.Aliyun aliyun, AcsRequest<T> describe) throws ClientException {
        IAcsClient client = buildAcsClient(regionId, aliyun);
        return client.getAcsResponse(describe);
    }

    public IAcsClient buildAcsClient(String regionId, EdsAliyunConfigModel.Aliyun aliyun) {
        String defRegionId = StringUtils.isEmpty(aliyun.getRegionId()) ? aliyun.getRegionId() : regionId;
        IClientProfile profile = DefaultProfile.getProfile(defRegionId, aliyun.getCred().getAccessKeyId(), aliyun.getCred().getAccessKeySecret());
        return new DefaultAcsClient(profile);
    }

}
