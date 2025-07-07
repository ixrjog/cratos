package com.baiyi.cratos.eds.huaweicloud.model;

import com.baiyi.cratos.domain.util.BeanCopierUtils;
import com.google.common.collect.Maps;
import com.huaweicloud.sdk.ecs.v2.model.ServerDetail;
import com.huaweicloud.sdk.ecs.v2.model.ServerSchedulerHints;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/10 上午11:09
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class HwcEcs {

    public static Ecs toEcs(String regionId, ServerDetail serverDetail) {
        // addresses
        Map<String, List<ServerAddress>> addresses = Maps.newHashMap();
        serverDetail.getAddresses()
                .keySet()
                .forEach(key -> {
                    List<ServerAddress> serverAddresses = serverDetail.getAddresses()
                            .get(key)
                            .stream()
                            .map(e -> ServerAddress.builder()
                                    .version(e.getVersion())
                                    .primary(e.getPrimary())
                                    .addr(e.getAddr())
                                    .osEXTIPSType(e.getOsEXTIPSType()
                                            .getValue())
                                    .osEXTIPSMACMacAddr(e.getOsEXTIPSMACMacAddr())
                                    .osEXTIPSPortId(e.getOsEXTIPSPortId())
                                    .build())
                            .toList();
                    addresses.put(key, serverAddresses);
                });
        // image
        ServerImage image = BeanCopierUtils.copyProperties(serverDetail.getImage(), ServerImage.class);
        // flavor
        ServerFlavor flavor = BeanCopierUtils.copyProperties(serverDetail.getFlavor(), ServerFlavor.class);
        // securityGroups
        List<ServerSecurityGroup> securityGroups = BeanCopierUtils.copyListProperties(serverDetail.getSecurityGroups(),
                ServerSecurityGroup.class);
        // fault;
        ServerFault fault = serverDetail.getFault() != null ? BeanCopierUtils.copyProperties(serverDetail.getFault(),
                ServerFault.class) : null;
        // osExtendedVolumesVolumesAttached
        List<ServerExtendVolumeAttachment> osExtendedVolumesVolumesAttached = BeanCopierUtils.copyListProperties(
                serverDetail.getOsExtendedVolumesVolumesAttached(), ServerExtendVolumeAttachment.class);
        // osSchedulerHints
        ServerSchedulerHints osSchedulerHints = BeanCopierUtils.copyProperties(serverDetail.getOsSchedulerHints(),
                ServerSchedulerHints.class);
        // sysTags
        List<ServerSystemTag> sysTags = BeanCopierUtils.copyListProperties(serverDetail.getSysTags(),
                ServerSystemTag.class);

        Server server = Server.builder()
                .status(serverDetail.getStatus())
                .updated(serverDetail.getUpdated())
                .autoTerminateTime(serverDetail.getAutoTerminateTime())
                .hostId(serverDetail.getHostId())
                .osEXTSRVATTRHost(serverDetail.getOsEXTSRVATTRHost())
                .addresses(addresses)
                .keyName(serverDetail.getKeyName())
                .image(image)
                .osEXTSTSTaskState(serverDetail.getOsEXTSTSTaskState())
                .osEXTSTSVmState(serverDetail.getOsEXTSTSVmState())
                .osEXTSRVATTRInstanceName(serverDetail.getOsEXTSRVATTRInstanceName())
                .osEXTSRVATTRHypervisorHostname(serverDetail.getOsEXTSRVATTRHypervisorHostname())
                .flavor(flavor)
                .id(serverDetail.getId())
                .securityGroups(securityGroups)
                .osEXTAZAvailabilityZone(serverDetail.getOsEXTAZAvailabilityZone())
                .userId(serverDetail.getUserId())
                .name(serverDetail.getName())
                .created(serverDetail.getCreated())
                .tenantId(serverDetail.getTenantId())
                .osDCFDiskConfig(serverDetail.getOsDCFDiskConfig())
                .accessIPv4(serverDetail.getAccessIPv4())
                .accessIPv6(serverDetail.getAccessIPv6())
                .fault(fault)
                .progress(serverDetail.getProgress())
                .osEXTSTSPowerState(serverDetail.getOsEXTSTSPowerState())
                .configDrive(serverDetail.getConfigDrive())
                .metadata(serverDetail.getMetadata())
                .osSRVUSGLaunchedAt(serverDetail.getOsSRVUSGLaunchedAt())
                .osSRVUSGTerminatedAt(serverDetail.getOsSRVUSGTerminatedAt())
                .osExtendedVolumesVolumesAttached(osExtendedVolumesVolumesAttached)
                .description(serverDetail.getDescription())
                .hostStatus(serverDetail.getHostStatus())
                .osEXTSRVATTRHostname(serverDetail.getOsEXTSRVATTRHostname())
                .osEXTSRVATTRReservationId(serverDetail.getOsEXTSRVATTRReservationId())
                .osEXTSRVATTRLaunchIndex(serverDetail.getOsEXTSRVATTRLaunchIndex())
                .osEXTSRVATTRKernelId(serverDetail.getOsEXTSRVATTRKernelId())
                .osEXTSRVATTRRamdiskId(serverDetail.getOsEXTSRVATTRRamdiskId())
                .osEXTSRVATTRRootDeviceName(serverDetail.getOsEXTSRVATTRRootDeviceName())
                .osEXTSRVATTRUserData(serverDetail.getOsEXTSRVATTRUserData())
                .locked(serverDetail.getLocked())
                .tags(serverDetail.getTags())
                .osSchedulerHints(osSchedulerHints)
                .enterpriseProjectId(serverDetail.getEnterpriseProjectId())
                .sysTags(sysTags)
                .build();
        return Ecs.builder()
                .regionId(regionId)
                .serverDetail(server)
                .build();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Ecs {
        private String regionId;
        private Server serverDetail;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Server {
        private String status;
        private String updated;
        private String autoTerminateTime;
        private String hostId;
        private String osEXTSRVATTRHost;
        // Local
        private Map<String, List<ServerAddress>> addresses;
        private String keyName;
        // Local
        private ServerImage image;
        private String osEXTSTSTaskState;
        private String osEXTSTSVmState;
        private String osEXTSRVATTRInstanceName;
        private String osEXTSRVATTRHypervisorHostname;
        // Local
        private ServerFlavor flavor;
        private String id;
        // Local
        private List<ServerSecurityGroup> securityGroups;
        private String osEXTAZAvailabilityZone;
        private String userId;
        private String name;
        private String created;
        private String tenantId;
        private String osDCFDiskConfig;
        private String accessIPv4;
        private String accessIPv6;
        // Local
        private ServerFault fault;
        private Integer progress;
        private Integer osEXTSTSPowerState;
        private String configDrive;
        private Map<String, String> metadata;
        private String osSRVUSGLaunchedAt;
        private String osSRVUSGTerminatedAt;
        // Local
        private List<ServerExtendVolumeAttachment> osExtendedVolumesVolumesAttached;
        private String description;
        private String hostStatus;
        private String osEXTSRVATTRHostname;
        private String osEXTSRVATTRReservationId;
        private Integer osEXTSRVATTRLaunchIndex;
        private String osEXTSRVATTRKernelId;
        private String osEXTSRVATTRRamdiskId;
        private String osEXTSRVATTRRootDeviceName;
        private String osEXTSRVATTRUserData;
        private Boolean locked;
        private List<String> tags;
        private ServerSchedulerHints osSchedulerHints;
        private String enterpriseProjectId;
        // Local
        private List<ServerSystemTag> sysTags;
        // private CpuOptions cpuOptions;
        // private Hypervisor hypervisor;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ServerImage {
        private String id;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ServerFlavor {
        private String id;
        private String name;
        private String disk;
        private String vcpus;
        private String ram;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ServerSecurityGroup {
        private String name;
        private String id;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ServerFault {
        private Integer code;
        private String created;
        private String message;
        private String details;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ServerAddress {
        private String version;
        private Boolean primary;
        private String addr;
        // private com.huaweicloud.sdk.ecs.v2.model.ServerAddress.OsEXTIPSTypeEnum osEXTIPSType;
        private String osEXTIPSType;
        private String osEXTIPSMACMacAddr;
        private String osEXTIPSPortId;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ServerExtendVolumeAttachment {
        private String id;
        private String deleteOnTermination;
        private String bootIndex;
        private String device;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ServerSystemTag {
        private String key;
        private String value;
    }

}
