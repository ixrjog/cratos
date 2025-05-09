package com.baiyi.cratos.eds.aliyun.management;

import com.aliyuncs.ecs.model.v20140526.*;
import com.aliyuncs.exceptions.ClientException;
import com.baiyi.cratos.eds.aliyun.client.common.AliyunClient;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/21 11:10
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliyunEcsInstanceManagement {

    private final AliyunClient aliyunClient;

    public void startInstance(String regionId, EdsAliyunConfigModel.Aliyun aliyun, String instanceId) {
        StartInstanceRequest request = new StartInstanceRequest();
        request.setSysRegionId(regionId);
        request.setInstanceId(instanceId);
        try {
            StartInstanceResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
        } catch (ClientException e) {
            log.error(e.getMessage());
        }
    }

    public void stopInstance(String regionId, EdsAliyunConfigModel.Aliyun aliyun, String instanceId) {
        StopInstanceRequest request = new StopInstanceRequest();
        request.setSysRegionId(regionId);
        request.setInstanceId(instanceId);
        try {
            StopInstanceResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
        } catch (ClientException e) {
            log.error(e.getMessage());
        }
    }

    public void rebootInstance(String regionId, EdsAliyunConfigModel.Aliyun aliyun, String instanceId) {
        RebootInstanceRequest request = new RebootInstanceRequest();
        request.setSysRegionId(regionId);
        request.setInstanceId(instanceId);
        try {
            RebootInstanceResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
        } catch (ClientException e) {
            log.error(e.getMessage());
        }
    }

}
