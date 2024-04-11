package com.baiyi.cratos.eds.aliyun.repo;

import com.aliyuncs.ecs.model.v20140526.*;
import com.aliyuncs.exceptions.ClientException;
import com.baiyi.cratos.eds.aliyun.client.AliyunClient;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/4/11 上午10:53
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliyunEcsRepo {

    private final AliyunClient aliyunClient;

    private static final int PAGE_SIZE = 50;

    public List<DescribeInstancesResponse.Instance> listInstances(String regionId, EdsAliyunConfigModel.Aliyun aliyun) {
        List<DescribeInstancesResponse.Instance> instanceList = Lists.newArrayList();
        String nextToken;
        try {
            DescribeInstancesRequest describe = new DescribeInstancesRequest();
            do {
                DescribeInstancesResponse response = aliyunClient.getAcsResponse(regionId, aliyun, describe);
                instanceList.addAll(response.getInstances());
                nextToken = response.getNextToken();
                describe.setNextToken(nextToken);
            } while (StringUtils.isNotBlank(nextToken));
        } catch (ClientException e) {
            log.error(e.getMessage());
        }
        return instanceList;
    }

    public List<DescribeImagesResponse.Image> listImages(String regionId, EdsAliyunConfigModel.Aliyun aliyun) {
        List<DescribeImagesResponse.Image> images = Lists.newArrayList();
        try {
            DescribeImagesRequest describe = new DescribeImagesRequest();
            describe.setSysRegionId(regionId);
            describe.setPageSize(PAGE_SIZE);
            /*
             * system：阿里云提供的公共镜像。
             * self：您创建的自定义镜像。
             * others：其他阿里云用户共享给您的镜像。
             * marketplace：镜像市场提供的镜像
             */
            describe.setImageOwnerAlias("self");
            int size = PAGE_SIZE;
            int pageNumber = 1;
            // 循环取值
            while (PAGE_SIZE <= size) {
                describe.setPageNumber(pageNumber);
                DescribeImagesResponse response = aliyunClient.getAcsResponse(regionId, aliyun, describe);
                images.addAll(response.getImages());
                size = response.getImages().size();
                pageNumber++;
            }
        } catch (ClientException e) {
            log.error(e.getMessage());
        }
        return images;
    }

    public List<DescribeDisksResponse.Disk> describeDisks(String regionId, EdsAliyunConfigModel.Aliyun aliyun, String instanceId) {
        try {
            DescribeDisksRequest describe = new DescribeDisksRequest();
            describe.setSysRegionId(regionId);
            describe.setInstanceId(instanceId);
            DescribeDisksResponse response = aliyunClient.getAcsResponse(regionId, aliyun, describe);
            return response.getDisks();
        } catch (ClientException e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

}
