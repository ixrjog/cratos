package com.baiyi.cratos.eds.aliyun.repo;

import com.aliyuncs.ecs.model.v20140526.DescribeVSwitchesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeVSwitchesResponse;
import com.aliyuncs.ecs.model.v20140526.DescribeVpcsRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeVpcsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baiyi.cratos.eds.aliyun.client.AliyunClient;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/19 下午2:19
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliyunVpcRepo {

    private final AliyunClient aliyunClient;

    private static final int PAGE_SIZE = 50;

    public List<DescribeVpcsResponse.Vpc> listVpc(String regionId, EdsAliyunConfigModel.Aliyun aliyun) {
        List<DescribeVpcsResponse.Vpc> vpcs = Lists.newArrayList();
        try {
            DescribeVpcsRequest describe = new DescribeVpcsRequest();
            describe.setSysRegionId(regionId);
            describe.setPageSize(PAGE_SIZE);
            int size = PAGE_SIZE;
            int pageNumber = 1;
            while (PAGE_SIZE <= size) {
                describe.setPageNumber(pageNumber);
                DescribeVpcsResponse response = aliyunClient.getAcsResponse(regionId, aliyun, describe);
                vpcs.addAll(response.getVpcs());
                size = response.getVpcs()
                        .size();
                pageNumber++;
            }
        } catch (ClientException e) {
            log.debug(e.getMessage());
        }
        return vpcs;
    }

    public List<DescribeVSwitchesResponse.VSwitch> listSwitch(String regionId, EdsAliyunConfigModel.Aliyun aliyun,
                                                                 String vpcId) {
        List<DescribeVSwitchesResponse.VSwitch> switches = Lists.newArrayList();
        try {
            DescribeVSwitchesRequest describe = new DescribeVSwitchesRequest();
            describe.setSysRegionId(regionId);
            describe.setPageSize(PAGE_SIZE);
            describe.setVpcId(vpcId);
            int size = PAGE_SIZE;
            int pageNumber = 1;
            while (PAGE_SIZE <= size) {
                describe.setPageNumber(pageNumber);
                DescribeVSwitchesResponse response = aliyunClient.getAcsResponse(regionId, aliyun, describe);
                switches.addAll(response.getVSwitches());
                size = response.getVSwitches()
                        .size();
                pageNumber++;
            }
        } catch (ClientException e) {
            log.debug(e.getMessage());
        }
        return switches;
    }

}
