package com.baiyi.cratos.eds.huaweicloud.stack.repo;

import com.baiyi.cratos.eds.core.config.EdsHcsConfigModel;
import com.baiyi.cratos.eds.huaweicloud.stack.client.HcsEcsClientBuilder;
import com.google.common.collect.Lists;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.ecs.v2.EcsClient;
import com.huaweicloud.sdk.ecs.v2.model.*;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/28 16:07
 * &#064;Version 1.0
 */
public class HcsEcsRepo {

    private static final int LIMIT = 100;

    public static List<ServerDetail> listServers(EdsHcsConfigModel.Hcs hcStack) throws ServiceResponseException {
        List<ServerDetail> serverDetails = Lists.newArrayList();
        EcsClient client = HcsEcsClientBuilder.buildEcsClient(hcStack);
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

    public static List<CloudServer> listCloudServers(EdsHcsConfigModel.Hcs hcStack) throws ServiceResponseException {
        List<CloudServer> serverDetails = Lists.newArrayList();
        EcsClient client = HcsEcsClientBuilder.buildEcsClient(hcStack);
        ListCloudServersRequest request = new ListCloudServersRequest();
        request.setLimit(LIMIT);
        int size = LIMIT;
        int pageNo = 1;
        while (LIMIT <= size) {
            ListCloudServersResponse response = client.listCloudServers(request);
            serverDetails.addAll(response.getServers());
            size = response.getServers()
                    .size();
            pageNo++;
        }
        return serverDetails;
    }

}
