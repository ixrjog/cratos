package com.baiyi.cratos.eds.aws.repo;


import com.amazonaws.services.elasticloadbalancingv2.model.DescribeLoadBalancersRequest;
import com.amazonaws.services.elasticloadbalancingv2.model.DescribeLoadBalancersResult;
import com.amazonaws.services.elasticloadbalancingv2.model.LoadBalancer;
import com.baiyi.cratos.eds.aws.service.AmazonElbService;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.google.common.collect.Lists;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/3/29 18:28
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AwsElbRepo {

    public static List<LoadBalancer> listLoadBalancer(String regionId, EdsAwsConfigModel.Aws aws) {
        DescribeLoadBalancersRequest request = new DescribeLoadBalancersRequest();
        // request.setPageSize(400);
        List<LoadBalancer> loadBalancers = Lists.newArrayList();
        while (true) {
            DescribeLoadBalancersResult result = AmazonElbService.buildAmazonELB(regionId, aws)
                    .describeLoadBalancers(request);
            loadBalancers.addAll(result.getLoadBalancers());
            if (StringUtils.hasText(result.getNextMarker())) {
                request.setMarker(result.getNextMarker());
            } else {
                return loadBalancers;
            }
        }
    }

}
