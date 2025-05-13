package com.baiyi.cratos.eds.cratos.model;

import com.baiyi.cratos.eds.core.annotation.AssetProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/9 13:52
 * &#064;Version 1.0
 */
public class CratosComputerModel {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Computer implements Serializable {
        @Serial
        private static final long serialVersionUID = -7555238264994084804L;
        @AssetProperty(typeOf = AssetProperty.Type.ASSET_ID)
        private String sn;
        @AssetProperty(typeOf = AssetProperty.Type.NAME)
        private String serverName;
        private String hostname;
        private String ipAddress;
        private String internalIp;
        private String serverType;
        private String status;
        private String region;
        private String zone;

        // 硬件信息
        private HardwareInfo hardwareInfo;

        // 系统信息
        private SystemInfo systemInfo;

        // 网络信息
        private NetworkInfo networkInfo;

        // 云服务信息
        private CloudInfo cloudInfo;

        // 时间戳
        private Date createdAt;
        private Date updatedAt;

        // 扩展信息
        private String notes;
        private List<CommonModel.Tag> tags;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HardwareInfo implements Serializable {
        @Serial
        private static final long serialVersionUID = 14934611769636383L;
        private String cpuModel;
        private Integer cpuCores;
        private Integer cpuThreads;
        private String memoryTotal;  // GB
        private String diskTotal;    // GB
        private String diskType;
        private Integer networkSpeed;    // Mbps
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SystemInfo implements Serializable {
        @Serial
        private static final long serialVersionUID = -6141021510167124706L;
        private String osType;
        private String osVersion;
        private String kernelVersion;
        private String architecture;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NetworkInfo implements Serializable {
        @Serial
        private static final long serialVersionUID = 268110218192646320L;
        private String domain;
        private String subnet;
        private String gateway;
        private String dnsServers;
        private String macAddress;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CloudInfo implements Serializable {
        @Serial
        private static final long serialVersionUID = 1651408238967343356L;
        private String cloudProvider;
        private String instanceId;
        private String instanceType;
        private String region;
        private String availabilityZone;
        private String vpcId;
    }

}
