package com.baiyi.cratos.eds.huaweicloud.cloud.model;

import com.baiyi.cratos.eds.core.config.base.HasRegionId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/1 11:35
 * &#064;Version 1.0
 */
public class HwcElb {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PoolRef {
        private String id;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ListenerRef {
        private String id;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Tag {
        private String key;
        private String value;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EipInfo {
        private String eipId;
        private String eipAddress;
        private Integer ipVersion;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PublicIpInfo {
        private String publicipId;
        private String publicipAddress;
        private Integer ipVersion;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GlobalEipInfo {
        private String globalEipId;
        private String globalEipAddress;
        private Integer ipVersion;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BandwidthRef {
        private String id;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AutoscalingRef {
        private Boolean enable;
        private String minL7FlavorId;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoadBalancer implements HasRegionId {

        public static LoadBalancer of(com.huaweicloud.sdk.elb.v3.model.LoadBalancer lb) {
            LoadBalancer result = LoadBalancer.builder()
                    .id(lb.getId())
                    .description(lb.getDescription())
                    .provisioningStatus(lb.getProvisioningStatus())
                    .adminStateUp(lb.getAdminStateUp())
                    .provider(lb.getProvider())
                    .operatingStatus(lb.getOperatingStatus())
                    .name(lb.getName())
                    .projectId(lb.getProjectId())
                    .vipSubnetCidrId(lb.getVipSubnetCidrId())
                    .vipAddress(lb.getVipAddress())
                    .vipPortId(lb.getVipPortId())
                    .createdAt(lb.getCreatedAt())
                    .updatedAt(lb.getUpdatedAt())
                    .guaranteed(lb.getGuaranteed())
                    .vpcId(lb.getVpcId())
                    .ipv6VipAddress(lb.getIpv6VipAddress())
                    .ipv6VipVirsubnetId(lb.getIpv6VipVirsubnetId())
                    .ipv6VipPortId(lb.getIpv6VipPortId())
                    .availabilityZoneList(lb.getAvailabilityZoneList())
                    .enterpriseProjectId(lb.getEnterpriseProjectId())
                    .billingInfo(lb.getBillingInfo())
                    .l4FlavorId(lb.getL4FlavorId())
                    .l4ScaleFlavorId(lb.getL4ScaleFlavorId())
                    .l7FlavorId(lb.getL7FlavorId())
                    .l7ScaleFlavorId(lb.getL7ScaleFlavorId())
                    .elbVirsubnetIds(lb.getElbVirsubnetIds())
                    .elbVirsubnetType(lb.getElbVirsubnetType() != null ? lb.getElbVirsubnetType().getValue() : null)
                    .ipTargetEnable(lb.getIpTargetEnable())
                    .frozenScene(lb.getFrozenScene())
                    .deletionProtectionEnable(lb.getDeletionProtectionEnable())
                    .publicBorderGroup(lb.getPublicBorderGroup())
                    .chargeMode(lb.getChargeMode())
                    .wafFailureAction(lb.getWafFailureAction())
                    .protectionStatus(lb.getProtectionStatus() != null ? lb.getProtectionStatus().getValue() : null)
                    .protectionReason(lb.getProtectionReason())
                    .logGroupId(lb.getLogGroupId())
                    .logTopicId(lb.getLogTopicId())
                    .build();
            // pools
            if (lb.getPools() != null) {
                result.setPools(lb.getPools().stream()
                        .map(e -> PoolRef.builder().id(e.getId()).build()).toList());
            }
            // listeners
            if (lb.getListeners() != null) {
                result.setListeners(lb.getListeners().stream()
                        .map(e -> ListenerRef.builder().id(e.getId()).build()).toList());
            }
            // tags
            if (lb.getTags() != null) {
                result.setTags(lb.getTags().stream()
                        .map(e -> Tag.builder().key(e.getKey()).value(e.getValue()).build()).toList());
            }
            // eips
            if (lb.getEips() != null) {
                result.setEips(lb.getEips().stream()
                        .map(e -> EipInfo.builder().eipId(e.getEipId()).eipAddress(e.getEipAddress()).ipVersion(e.getIpVersion()).build()).toList());
            }
            // publicips
            if (lb.getPublicips() != null) {
                result.setPublicips(lb.getPublicips().stream()
                        .map(e -> PublicIpInfo.builder().publicipId(e.getPublicipId()).publicipAddress(e.getPublicipAddress()).ipVersion(e.getIpVersion()).build()).toList());
            }
            // globalEips
            if (lb.getGlobalEips() != null) {
                result.setGlobalEips(lb.getGlobalEips().stream()
                        .map(e -> GlobalEipInfo.builder().globalEipId(e.getGlobalEipId()).globalEipAddress(e.getGlobalEipAddress()).ipVersion(e.getIpVersion()).build()).toList());
            }
            // ipv6Bandwidth
            if (lb.getIpv6Bandwidth() != null) {
                result.setIpv6Bandwidth(BandwidthRef.builder().id(lb.getIpv6Bandwidth().getId()).build());
            }
            // autoscaling
            if (lb.getAutoscaling() != null) {
                result.setAutoscaling(AutoscalingRef.builder().enable(lb.getAutoscaling().getEnable()).minL7FlavorId(lb.getAutoscaling().getMinL7FlavorId()).build());
            }
            return result;
        }

        private String regionId;

        // ---------------
        private String id;
        private String description;
        private String provisioningStatus;
        private Boolean adminStateUp;
        private String provider;
        private List<PoolRef> pools;
        private List<ListenerRef> listeners;
        private String operatingStatus;
        private String name;
        private String projectId;
        private String vipSubnetCidrId;
        private String vipAddress;
        private String vipPortId;
        private List<Tag> tags;
        private String createdAt;
        private String updatedAt;
        private Boolean guaranteed;
        private String vpcId;
        private List<EipInfo> eips;
        private String ipv6VipAddress;
        private String ipv6VipVirsubnetId;
        private String ipv6VipPortId;
        private List<String> availabilityZoneList;
        private String enterpriseProjectId;
        private String billingInfo;
        private String l4FlavorId;
        private String l4ScaleFlavorId;
        private String l7FlavorId;
        private String l7ScaleFlavorId;
        private List<PublicIpInfo> publicips;
        private List<GlobalEipInfo> globalEips;
        private List<String> elbVirsubnetIds;
        private String elbVirsubnetType;
        private Boolean ipTargetEnable;
        private String frozenScene;
        private BandwidthRef ipv6Bandwidth;
        private Boolean deletionProtectionEnable;
        private AutoscalingRef autoscaling;
        private String publicBorderGroup;
        private String chargeMode;
        private String wafFailureAction;
        private String protectionStatus;
        private String protectionReason;
        private String logGroupId;
        private String logTopicId;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Cert implements HasRegionId {
        private String regionId;
        private Boolean adminStateUp;
        //  private String certificate;
        private String description;
        private String domain;
        private String id;
        private String name;
        // private String privateKey;
        private String type;
        private String createdAt;
        private String updatedAt;
        private String expireTime;
        private String projectId;
        private String encCertificate;
        private String encPrivateKey;
        private String scmCertificateId;
        private String commonName;
        private String fingerprint;
        private List<String> subjectAlternativeNames;
    }

}
