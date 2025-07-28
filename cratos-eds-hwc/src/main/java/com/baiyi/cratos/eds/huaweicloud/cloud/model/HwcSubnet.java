package com.baiyi.cratos.eds.huaweicloud.cloud.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/10/31 15:42
 * &#064;Version 1.0
 */
public class HwcSubnet {

    public static Subnet toSubnet(String regionId, String projectId, com.huaweicloud.sdk.vpc.v2.model.Subnet subnet) {
        SubnetModel subnetModel = SubnetModel.builder()
                .id(subnet.getId())
                .name(subnet.getName())
                .description(subnet.getDescription())
                .cidr(subnet.getCidr())
                .gatewayIp(subnet.getGatewayIp())
                .ipv6Enable(subnet.getIpv6Enable())
                .gatewayIpV6(subnet.getGatewayIpV6())
                .dhcpEnable(subnet.getDhcpEnable())
                .primaryDns(subnet.getPrimaryDns())
                .secondaryDns(subnet.getSecondaryDns())
                .dnsList(subnet.getDnsList())
                .availabilityZone(subnet.getAvailabilityZone())
                .vpcId(subnet.getVpcId())
                .status(subnet.getStatus()
                        .getValue())
                .neutronNetworkId(subnet.getNeutronSubnetId())
                .neutronSubnetId(subnet.getNeutronSubnetId())
                .neutronSubnetIdV6(subnet.getNeutronSubnetIdV6())
                // private List<ExtraDhcpOption> extraDhcpOpts;
                .scope(subnet.getScope())
                .tenantId(subnet.getTenantId())
                .createdAt(Date.from(subnet.getCreatedAt()
                        .toInstant()))
                .updatedAt(Date.from(subnet.getUpdatedAt()
                        .toInstant()))
                .build();
        return Subnet.builder()
                .regionId(regionId)
                .subnet(subnetModel)
                .build();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Subnet {
        private String regionId;
        private SubnetModel subnet;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SubnetModel {
        private String id;
        private String name;
        private String description;
        private String cidr;
        private String gatewayIp;
        private Boolean ipv6Enable;
        private String cidrV6;
        private String gatewayIpV6;
        private Boolean dhcpEnable;
        private String primaryDns;
        private String secondaryDns;
        private List<String> dnsList;
        private String availabilityZone;
        private String vpcId;
        private String status;
        private String neutronNetworkId;
        private String neutronSubnetId;
        private String neutronSubnetIdV6;
        // private List<ExtraDhcpOption> extraDhcpOpts;
        private String scope;
        private String tenantId;
        private Date createdAt;
        private Date updatedAt;
    }

}
