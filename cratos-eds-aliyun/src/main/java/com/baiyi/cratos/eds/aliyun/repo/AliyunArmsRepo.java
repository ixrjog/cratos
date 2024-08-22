package com.baiyi.cratos.eds.aliyun.repo;

import com.aliyun.sdk.service.arms20190808.AsyncClient;
import com.aliyun.sdk.service.arms20190808.models.ListTraceAppsRequest;
import com.aliyun.sdk.service.arms20190808.models.ListTraceAppsResponse;
import com.aliyun.sdk.service.arms20190808.models.ListTraceAppsResponseBody;
import com.baiyi.cratos.eds.aliyun.client.AliyunArmsClient;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/1 上午10:43
 * &#064;Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AliyunArmsRepo {

    public static List<ListTraceAppsResponseBody.TraceApps> listTraceApps(
            EdsAliyunConfigModel.Aliyun aliyun) throws Exception {
        return listTraceApps(aliyun.getArms()
                .getRegionId(), aliyun);
    }

    public static List<ListTraceAppsResponseBody.TraceApps> listTraceApps(String regionId,
                                                                          EdsAliyunConfigModel.Aliyun aliyun) throws Exception {
        List<ListTraceAppsResponseBody.TraceApps> result = Lists.newArrayList();
        try (AsyncClient client = AliyunArmsClient.buildAsyncClient(regionId, aliyun)) {
            ListTraceAppsRequest listProjectsRequest = ListTraceAppsRequest.builder()
                    .regionId(regionId)
                    .build();
            CompletableFuture<ListTraceAppsResponse> response = client.listTraceApps(listProjectsRequest);
            return response.get()
                    .getBody()
                    .getTraceApps();
        } catch (InterruptedException interruptedException) {
            Thread.currentThread()
                    .interrupt();
            throw interruptedException;
        } catch (ExecutionException executionException) {
            log.debug(executionException.getMessage());
            throw executionException;
        }
    }

}
