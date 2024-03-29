package com.baiyi.cratos.eds.aliyun.repo;

import com.aliyun.alb20200616.models.ListLoadBalancersRequest;
import com.aliyun.alb20200616.models.ListLoadBalancersResponse;
import com.aliyun.alb20200616.models.ListLoadBalancersResponseBody;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2024/3/28 17:38
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AliyunAlbRepo {

    /**
     * 使用AK&SK初始化账号Client
     *
     * @param aliyun
     * @return
     * @throws Exception
     */
    public static com.aliyun.alb20200616.Client createClient(String endpoint, EdsAliyunConfigModel.Aliyun aliyun) throws Exception {
        // 工程代码泄露可能会导致 AccessKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考。
        // 建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html。
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID。
                .setAccessKeyId(aliyun.getCred()
                        .getAccessKeyId())
                // 必填，请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
                .setAccessKeySecret(aliyun.getCred()
                        .getAccessKeySecret());
        // Endpoint 请参考 https://api.aliyun.com/product/Alb
        // alb.cn-hangzhou.aliyuncs.com
        // alb.eu-central-1.aliyuncs.com
        config.endpoint = endpoint;
        return new com.aliyun.alb20200616.Client(config);
    }

    public List<ListLoadBalancersResponseBody.ListLoadBalancersResponseBodyLoadBalancers> listAlb(String endpoint, EdsAliyunConfigModel.Aliyun aliyun) throws Exception {
        ListLoadBalancersRequest request = new ListLoadBalancersRequest();
        List<ListLoadBalancersResponseBody.ListLoadBalancersResponseBodyLoadBalancers> albList = Lists.newArrayList();
        com.aliyun.alb20200616.Client client = AliyunAlbRepo.createClient(endpoint, aliyun);
        while (true) {
            ListLoadBalancersResponse response = client.listLoadBalancers(request);
            response.getBody()
                    .getLoadBalancers();
            List<ListLoadBalancersResponseBody.ListLoadBalancersResponseBodyLoadBalancers> results = Optional.of(response)
                    .map(ListLoadBalancersResponse::getBody)
                    .map(ListLoadBalancersResponseBody::getLoadBalancers)
                    .orElse(Collections.emptyList());
            if (CollectionUtils.isEmpty(results)) {
                break;
            } else {
                albList.addAll(results);
            }
            String nextToken = Optional.of(response)
                    .map(ListLoadBalancersResponse::getBody)
                    .map(ListLoadBalancersResponseBody::getNextToken)
                    .orElse("");
            if (StringUtils.hasText(nextToken)) {
                request.setNextToken(nextToken);
            } else {
                break;
            }
        }
        return albList;
    }

}
