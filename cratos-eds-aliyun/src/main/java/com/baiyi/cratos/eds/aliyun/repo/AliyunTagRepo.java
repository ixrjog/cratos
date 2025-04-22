package com.baiyi.cratos.eds.aliyun.repo;

import com.aliyuncs.ecs.model.v20140526.ListTagResourcesRequest;
import com.aliyuncs.ecs.model.v20140526.ListTagResourcesResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baiyi.cratos.eds.aliyun.client.AliyunClient;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/18 16:01
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliyunTagRepo {

    private final AliyunClient aliyunClient;

    public enum ResourceTypes {
        INSTANCE
    }

    public List<ListTagResourcesResponse.TagResource> listTagResources(String regionId,
                                                                       EdsAliyunConfigModel.Aliyun aliyun,
                                                                       ResourceTypes resourceType, String resourceId) {
        ListTagResourcesRequest request = new ListTagResourcesRequest();
        request.setResourceType(resourceType.name()
                .toLowerCase());
        request.setResourceIds(List.of(resourceId));
        List<ListTagResourcesResponse.TagResource> tagResources = Lists.newArrayList();
        String nextToken = null;
        try {
            do {
                request.setNextToken(nextToken);
                ListTagResourcesResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
                if (response != null && !CollectionUtils.isEmpty(response.getTagResources())) {
                    tagResources.addAll(response.getTagResources());
                }
                nextToken = response != null ? response.getNextToken() : null;
            } while (StringUtils.isNotBlank(nextToken));
        } catch (ClientException e) {
            log.error("Failed to list tag resources for regionId: {}, error: {}", regionId, e.getMessage(), e);
        }
        return tagResources.isEmpty() ? Collections.emptyList() : tagResources;
    }

}
