package com.baiyi.cratos.eds.huaweicloud.cloud.repo;

import com.baiyi.cratos.eds.core.config.EdsHwcConfigModel;
import com.baiyi.cratos.eds.huaweicloud.cloud.client.HwcEcsClientBuilder;
import com.google.common.collect.Lists;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.ecs.v2.EcsClient;
import com.huaweicloud.sdk.ecs.v2.model.*;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/10 上午10:26
 * &#064;Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class HwcEcsRepo {

    private static final int LIMIT = 100;

    public static List<ServerDetail> listServers(String regionId,
                                                 EdsHwcConfigModel.Hwc huaweicloud) throws ServiceResponseException {
        List<ServerDetail> serverDetails = Lists.newArrayList();
        EcsClient client = HwcEcsClientBuilder.buildEcsClient(regionId, huaweicloud);
        ListServersDetailsRequest request = new ListServersDetailsRequest();
        request.setLimit(LIMIT);
        int size = LIMIT;
        int pageNo = 1;
        while (LIMIT <= size) {
            ListServersDetailsResponse response = client.listServersDetails(request);
            serverDetails.addAll(response.getServers());
            size = response.getServers()
                    .size();
            pageNo++;
        }
        return serverDetails;
    }

    public static String rebootInstance(String regionId, EdsHwcConfigModel.Hwc huaweicloud,
                                        String instanceId) throws ServiceResponseException {
        EcsClient client = HwcEcsClientBuilder.buildEcsClient(regionId, huaweicloud);

        BatchRebootSeversOption reboot = new BatchRebootSeversOption();
        reboot.setType(BatchRebootSeversOption.TypeEnum.HARD);
        reboot.setServers(List.of(new ServerId().withId(instanceId)));

        BatchRebootServersRequestBody body = new BatchRebootServersRequestBody();
        body.setReboot(reboot);

        BatchRebootServersRequest request = new BatchRebootServersRequest();
        request.setBody(body);

        BatchRebootServersResponse response = client.batchRebootServers(request);
        return Optional.ofNullable(response)
                .map(BatchRebootServersResponse::getJobId)
                .orElse(null);
    }

    public static String startInstance(String regionId, EdsHwcConfigModel.Hwc huaweicloud,
                                       String instanceId) throws ServiceResponseException {
        EcsClient client = HwcEcsClientBuilder.buildEcsClient(regionId, huaweicloud);

        BatchStartServersRequestBody body = new BatchStartServersRequestBody();
        BatchStartServersOption start = new BatchStartServersOption();
        start.setServers(List.of(new ServerId().withId(instanceId)));
        body.setOsStart(start);

        BatchStartServersRequest request = new BatchStartServersRequest();
        request.setBody(body);

        BatchStartServersResponse response = client.batchStartServers(request);
        return Optional.ofNullable(response)
                .map(BatchStartServersResponse::getJobId)
                .orElse(null);
    }

    public static String stopInstance(String regionId, EdsHwcConfigModel.Hwc huaweicloud,
                                      String instanceId) throws ServiceResponseException {
        EcsClient client = HwcEcsClientBuilder.buildEcsClient(regionId, huaweicloud);

        BatchStopServersRequestBody body = new BatchStopServersRequestBody();
        BatchStopServersOption stop = new BatchStopServersOption();
        stop.setServers(List.of(new ServerId().withId(instanceId)));
        body.setOsStop(stop);

        BatchStopServersRequest request = new BatchStopServersRequest();
        request.setBody(body);

        BatchStopServersResponse response = client.batchStopServers(request);
        return Optional.ofNullable(response)
                .map(BatchStopServersResponse::getJobId)
                .orElse(null);
    }

}
