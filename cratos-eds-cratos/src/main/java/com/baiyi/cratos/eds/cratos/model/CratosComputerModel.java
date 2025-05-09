package com.baiyi.cratos.eds.cratos.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

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

        private Long id;
        private String serverName;
        private String hostname;
        private String ipAddress;
        private String internalIp;
        private ServerType serverType;
        private ServerStatus status;
        private Environment environment;

        // 硬件信息
        private String cpuModel;
        private Integer cpuCores;
        private Integer cpuThreads;
//        private BigDecimal memoryTotal;  // GB
//        private BigDecimal diskTotal;    // GB
        private String diskType;
        private Integer networkSpeed;    // Mbps

        // 系统信息
        private String osType;
        private String osVersion;
        private String kernelVersion;
        private String architecture;

        // 管理信息
        private Integer departmentId;
        private Integer ownerId;
        private String purpose;
        private String location;
        private String rackPosition;

        // 网络信息
        private String domain;
        private String subnet;
        private String gateway;
        private String dnsServers;  // 可以是JSON字符串或者转换为Map
        private String macAddress;

        // 安全信息
        private Boolean firewallEnabled;
        private Integer sshPort;
        private String accessMethod;
        private String securityGroup;

        // 监控和维护
        private Boolean monitoringEnabled;
        private Boolean backupEnabled;
        private Integer uptime;  // 运行天数

        // 云服务信息
        private String cloudProvider;
        private String instanceId;
        private String instanceType;
        private String region;
        private String availabilityZone;
        private String vpcId;

        // 时间戳
        private Date createdAt;
        private  Date updatedAt;

        // 扩展信息
        private String tags;  // 可以是JSON字符串或者转换为Map
        private String notes;
        private String customAttributes;  // 可以是JSON字符串或者转换为Map

    }

    // 枚举定义
    public enum ServerType {
        PHYSICAL, VIRTUAL, CONTAINER, CLOUD
    }

    public enum ServerStatus {
        RUNNING, STOPPED, MAINTENANCE, FAILED
    }

    public enum Environment {
        PRODUCTION, TESTING, DEVELOPMENT, STAGING
    }


}
