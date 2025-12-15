package com.baiyi.cratos.eds.aliyun.repo;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ram.model.v20150501.CreateAccessKeyRequest;
import com.aliyuncs.ram.model.v20150501.CreateAccessKeyResponse;
import com.aliyuncs.ram.model.v20150501.ListAccessKeysRequest;
import com.aliyuncs.ram.model.v20150501.ListAccessKeysResponse;
import com.baiyi.cratos.eds.aliyun.client.common.AliyunClient;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/5 15:19
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliyunRamAccessKeyRepo {

    private final AliyunClient aliyunClient;

    public List<ListAccessKeysResponse.AccessKey> listAccessKeys(EdsConfigs.Aliyun aliyun, String username) {
        try {
            ListAccessKeysRequest request = new ListAccessKeysRequest();
            request.setUserName(username);
            ListAccessKeysResponse response = aliyunClient.getAcsResponse(aliyun.getRegionId(), aliyun, request);
            return response.getAccessKeys();
        } catch (ClientException e) {
            log.debug(e.getMessage());
        }
        return List.of();
    }

    public CreateAccessKeyResponse.AccessKey createAccessKey(EdsConfigs.Aliyun aliyun,
                                                             String username) throws ClientException {
        CreateAccessKeyRequest request = new CreateAccessKeyRequest();
        request.setUserName(username);
        CreateAccessKeyResponse response = aliyunClient.getAcsResponse(aliyun.getRegionId(), aliyun, request);
        return response.getAccessKey();
    }

}
