package com.baiyi.cratos.eds.ucloud.global.repo;

import cn.ucloud.uhost.client.UHostClient;
import cn.ucloud.uhost.models.DescribeUHostInstanceRequest;
import cn.ucloud.uhost.models.DescribeUHostInstanceResponse;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.ucloud.global.client.UcloudUHostClient;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/6 10:35
 * &#064;Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UcloudGlobalUHostRepo {

    public static List<DescribeUHostInstanceResponse.UHostInstanceSet> describeUHostInstance(String regionId,
                                                                                             EdsConfigs.Ucloud ucloud,
                                                                                             String projectId) throws Exception {
        try (UHostClient uHostClient = UcloudUHostClient.createClient(regionId, ucloud)) {
            DescribeUHostInstanceRequest request = new DescribeUHostInstanceRequest();
            request.setRegion(regionId);
            if (StringUtils.hasText(projectId)) {
                request.setProjectId(projectId);
            }
            List<DescribeUHostInstanceResponse.UHostInstanceSet> result = Lists.newArrayList();
            int offset = 0;
            while (true) {
                request.setOffset(offset);
                DescribeUHostInstanceResponse response = uHostClient.describeUHostInstance(request);
                List<DescribeUHostInstanceResponse.UHostInstanceSet> uHosts = Optional.ofNullable(response)
                        .map(DescribeUHostInstanceResponse::getUHostSet)
                        .orElse(List.of());
                if (CollectionUtils.isEmpty(uHosts)) {
                    break;
                }
                result.addAll(uHosts);
                int totalCount = Optional.of(response)
                        .map(DescribeUHostInstanceResponse::getTotalCount)
                        .orElse(0);
                if (result.size() >= totalCount) {
                    break;
                }
                offset = result.size();
            }
            return result;
        }
    }

}
