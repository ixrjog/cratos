package com.baiyi.cratos.eds.zabbix.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/29 11:35
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ZbxHostResult {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Host implements Serializable {
        @Serial
        private static final long serialVersionUID = -3998141908332362005L;
        private String hostid;
        /**
         * 主机的正式名称
         */
        private String host;

        private String description;
        /**
         * 主机的来源。
         * 0 - 普通主机；
         * 4 - 自动发现的主机。
         */
        private Integer flags;
        /**
         * 主机可见名
         */
        private String name;
        /**
         * 主机的状态。
         * 0 - (默认) 已监控的主机；
         * 1 - 未监控的主机。
         */
        private Integer status;
        /**
         * 主机资产清单填充模式
         * -1 - (默认) 禁用;
         * 0 - 手动;
         * 1 - 自动。
         */
        @JsonProperty("inventory_mode")
        private Integer inventoryMode;
        private String proxyid;
        @JsonProperty("proxy_groupid")
        private String proxyGroupid;
        /**
         * 主机活动接口可用性状态
         * 0 - 接口状态未知;
         * 1 - 接口可用;
         * 2 - 接口不可用.
         */
        @JsonProperty("active_available")
        private Integer activeAvailable;
        /**
         * 用于监控主机的源
         * 0 - (默认) Zabbix server;
         * 1 - Proxy;
         * 2 - proxy 组.
         */
        @JsonProperty("monitored_by")
        private Integer monitoredBy;
        private List<ZbxCommonResult.Tag> tags;
        private List<ZbxCommonResult.Tag> inheritedTags;

        private HostExtend hostExtend;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HostExtend implements Serializable {
        @Serial
        private static final long serialVersionUID = 2088562884491010308L;
        private String hostid;
        private List<ZbxTemplateResult.Template> parentTemplates;
        private List<ZbxHostGroupResult.HostGroup> hostgroups;
        private List<ZbxInterfaceResult.Interface> interfaces;
    }

}
