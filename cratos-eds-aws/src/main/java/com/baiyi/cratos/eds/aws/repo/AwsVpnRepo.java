package com.baiyi.cratos.eds.aws.repo;

import com.amazonaws.services.ec2.model.DescribeVpnConnectionsRequest;
import com.amazonaws.services.ec2.model.DescribeVpnConnectionsResult;
import com.amazonaws.services.ec2.model.VpnConnection;
import com.baiyi.cratos.eds.aws.service.AmazonEc2Service;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/3/4 11:22
 * @Version 1.0
 */
@Schema(description = "AWS Site-to-Site VPN")
@NoArgsConstructor(access = PRIVATE)
public class AwsVpnRepo {

    public static List<VpnConnection> describeVpnConnections(String regionId, EdsAwsConfigModel.Aws aws) {
        DescribeVpnConnectionsRequest request = new DescribeVpnConnectionsRequest();
        DescribeVpnConnectionsResult result = AmazonEc2Service.buildAmazonEC2(regionId, aws)
                .describeVpnConnections(request);
        return result.getVpnConnections();
    }

}
