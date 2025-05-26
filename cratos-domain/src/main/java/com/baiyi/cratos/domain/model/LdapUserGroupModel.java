package com.baiyi.cratos.domain.model;

import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/26 15:04
 * &#064;Version 1.0
 */
public class LdapUserGroupModel {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Role implements Serializable {
        @Serial
        private static final long serialVersionUID = 6028901986287498506L;
        private EdsAssetVO.Asset asset;
        private String name;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class LdapRoleOptions implements Serializable {
        @Serial
        private static final long serialVersionUID = 4939142954388880896L;

        public static final LdapRoleOptions DATA = LdapRoleOptions.builder()
                .build();

        @Builder.Default
        private Map<String, List<String>> groupMembers = Map.of(
                Group.NEXUS.name(),
                List.of("nexus-admin", "nexus-developer", "nexus-viewer"), Group.VPN.name(),
                List.of("vpn-admin", "vpn-user"), Group.APOLLO.name(),
                List.of("apollo-admin", "apollo-developer", "apollo-viewer"), Group.NACOS.name(),
                List.of("nacos-admin", "nacos-developer", "nacos-viewer"), Group.GRAFANA.name(),
                List.of("grafana-admin", "grafana-editor", "grafana-viewer"));
    }

    @Getter
    public enum Group {

        NEXUS("Nexus", "Nexus Repository Manager"),
        VPN("VPN", "VPN"),
        APOLLO("Apollo", "Apollo Config Service"),
        NACOS("Nacos", "Nacos Config Service"),
        GRAFANA("Grafana", "Grafana Monitoring Service");

        private final String displayName;

        private final String desc;

        Group(String displayName, String desc) {
            this.displayName = displayName;
            this.desc = desc;
        }

        public Group displayNameOf(String displayName) {
            for (Group ldapGroup : Group.values()) {
                if (ldapGroup.getDisplayName()
                        .equals(displayName)) {
                    return ldapGroup;
                }
            }
            return null;
        }

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class LdapUserGroup implements Serializable {
        @Serial
        private static final long serialVersionUID = 6970366679820904698L;

        public static final LdapUserGroup NEXUS_ADMIN = LdapUserGroup.builder()
                .name("nexus-admin")
                .displayName("Nexus Admin")
                .desc("Nexus Repository Manager Administrator")
                .docs("--")
                .build();

        public static final LdapUserGroup NEXUS_USERS = LdapUserGroup.builder()
                .name("nexus-users")
                .displayName("Nexus Users")
                .desc("Nexus Repository Manager Users")
                .docs("--")
                .build();

        private String name;
        private String displayName;
        private String desc;
        private String docs;
    }

}
