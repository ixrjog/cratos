package com.baiyi.cratos.eds.huaweicloud.cloud.repo;

import com.baiyi.cratos.eds.core.config.EdsHwcConfigModel;
import com.baiyi.cratos.eds.huaweicloud.cloud.client.HwcVpcClientBuilder;
import com.google.common.collect.Lists;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.vpc.v2.VpcClient;
import com.huaweicloud.sdk.vpc.v2.model.*;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/10/31 10:12
 * &#064;Version 1.0
 */
public class HwcVpcRepo {

    private static final int LIMIT = 2000;

    public static List<Vpc> listVpcs(String regionId,
                                    EdsHwcConfigModel.Hwc huaweicloud) throws ServiceResponseException {
        List<Vpc> vpcs = Lists.newArrayList();
        VpcClient client = HwcVpcClientBuilder.buildVpcClient(regionId, huaweicloud);
        ListVpcsRequest request = new ListVpcsRequest();
        request.setLimit(LIMIT);
        int size = LIMIT;
        int pageNo = 1;
        while (LIMIT <= size) {
            ListVpcsResponse response = client.listVpcs(request);
            vpcs.addAll(response.getVpcs());
            size = response.getVpcs()
                    .size();
            pageNo++;
        }
        return vpcs;
    }

    public static List<Subnet> listSubnets(String regionId,
                                     EdsHwcConfigModel.Hwc huaweicloud) throws ServiceResponseException {
        List<Subnet> subnets = Lists.newArrayList();
        VpcClient client = HwcVpcClientBuilder.buildVpcClient(regionId, huaweicloud);
        ListSubnetsRequest request = new ListSubnetsRequest();
        request.setLimit(LIMIT);
        int size = LIMIT;
        int pageNo = 1;
        while (LIMIT <= size) {
            ListSubnetsResponse response = client.listSubnets(request);
            subnets.addAll(response.getSubnets());
            size = response.getSubnets()
                    .size();
            pageNo++;
        }
        return subnets;
    }

}
