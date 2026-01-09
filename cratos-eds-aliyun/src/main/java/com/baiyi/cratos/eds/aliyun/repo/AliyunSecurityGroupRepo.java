package com.baiyi.cratos.eds.aliyun.repo;

import com.aliyuncs.ecs.model.v20140526.DescribeSecurityGroupAttributeRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeSecurityGroupAttributeResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baiyi.cratos.eds.aliyun.client.common.AliyunClient;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
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
 * &#064;Date  2026/1/9 16:58
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliyunSecurityGroupRepo {

    private final AliyunClient aliyunClient;

    public List<DescribeSecurityGroupAttributeResponse.Permission> describeSecurityGroupAttribute(String regionId,
                                                                                                  EdsConfigs.Aliyun aliyun,String securityGroupId) {
        List<DescribeSecurityGroupAttributeResponse.Permission> permissions = Lists.newArrayList();
        String nextToken;
        DescribeSecurityGroupAttributeRequest describe = new DescribeSecurityGroupAttributeRequest();
        describe.setSecurityGroupId(securityGroupId);
        try {
            do {
                DescribeSecurityGroupAttributeResponse response = aliyunClient.getAcsResponse(
                        regionId, aliyun, describe);
                if (response != null && !CollectionUtils.isEmpty(response.getPermissions())) {
                    permissions.addAll(response.getPermissions());
                }
                nextToken = response != null ? response.getNextToken() : null;
                describe.setNextToken(nextToken);
            } while (StringUtils.isNotBlank(nextToken));
        } catch (ClientException e) {
            log.error("Failed to describe SecurityGroup attribute for regionId: {}, error: {}", regionId, e.getMessage(), e);
        }
        return permissions.isEmpty() ? Collections.emptyList() : permissions;
    }

}
