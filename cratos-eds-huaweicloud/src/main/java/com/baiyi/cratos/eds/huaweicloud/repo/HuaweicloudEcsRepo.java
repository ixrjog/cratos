package com.baiyi.cratos.eds.huaweicloud.repo;

import com.baiyi.cratos.eds.core.config.EdsHuaweicloudConfigModel;
import com.google.common.collect.Lists;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.core.http.HttpConfig;
import com.huaweicloud.sdk.ecs.v2.EcsClient;
import com.huaweicloud.sdk.ecs.v2.model.ListServersDetailsRequest;
import com.huaweicloud.sdk.ecs.v2.model.ListServersDetailsResponse;
import com.huaweicloud.sdk.ecs.v2.model.ServerDetail;
import com.huaweicloud.sdk.ecs.v2.region.EcsRegion;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/10 上午10:26
 * &#064;Version 1.0
 */
@Slf4j
public class HuaweicloudEcsRepo {

    private static final int LIMIT = 100;

    public static List<ServerDetail> listServers(String regionId,
                                                 EdsHuaweicloudConfigModel.Huaweicloud huaweicloud) throws ServiceResponseException {
        List<ServerDetail> serverDetails = Lists.newArrayList();
        EcsClient client = buildEcsClient(regionId, huaweicloud);
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

    public static EcsClient buildEcsClient(String regionId, EdsHuaweicloudConfigModel.Huaweicloud huaweicloud) {
        // 配置客户端属性
        HttpConfig config = HttpConfig.getDefaultHttpConfig();
        config.withIgnoreSSLVerification(true);

        // 创建认证
        BasicCredentials auth = new BasicCredentials().withAk(huaweicloud.getCred()
                        .getAccessKey())
                .withSk(huaweicloud.getCred()
                        .getSecretKey());
        return EcsClient.newBuilder()
                .withHttpConfig(config)
                .withCredential(auth)
                .withRegion(EcsRegion.valueOf(regionId))
                .build();
    }

}
