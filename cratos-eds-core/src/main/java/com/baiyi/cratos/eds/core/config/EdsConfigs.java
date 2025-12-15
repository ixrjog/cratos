package com.baiyi.cratos.eds.core.config;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.eds.core.config.base.HasDnsNameServers;
import com.baiyi.cratos.eds.core.config.base.HasRegionsModel;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import com.baiyi.cratos.eds.core.config.model.*;
import com.google.common.base.Joiner;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

import static com.baiyi.cratos.eds.core.config.model.EdsHarborConfigModel.API_V2;
import static com.baiyi.cratos.eds.core.config.model.EdsLdapConfigModel.RDN;
import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/15 13:51
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class EdsConfigs {

    @Data
    @NoArgsConstructor
    @Schema
    public static class Aliyun implements HasRegionsModel, IEdsConfigModel {
        private String version;
        // default
        private String regionId;
        private Set<String> regionIds;
        @Schema(description = "凭据")
        private EdsAliyunConfigModel.Cred cred;
        private EdsAliyunConfigModel.ALB alb;
        private EdsAliyunConfigModel.NLB nlb;
        private EdsAliyunConfigModel.OSS oss;
        private EdsAliyunConfigModel.Domain domain;
        private EdsInstance edsInstance;
        private EdsAliyunConfigModel.ARMS arms;
        private EdsAliyunConfigModel.MongoDB mongoDB;
        private EdsAliyunConfigModel.ONS ons;
        private EdsAliyunConfigModel.ACR acr;
        private EdsAliyunConfigModel.RAM ram;
        private EdsAliyunConfigModel.KMS kms;
        private EdsAliyunConfigModel.DMS dms;
        private EdsAliyunConfigModel.DNS dns;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Alimail implements IEdsConfigModel {
        private String version;
        @Schema(description = "凭据")
        private EdsAlimailConfigModel.Cred cred;
        private EdsAlimailConfigModel.Api api;
        private String loginUrl;
        private EdsInstance edsInstance;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Aws implements HasRegionsModel, IEdsConfigModel {
        private EdsAwsConfigModel.Cred cred;
        private String regionId;
        private Set<String> regionIds;
        private EdsAwsConfigModel.EC2 ec2;
        private EdsAwsConfigModel.IAM iam;
        private EdsInstance edsInstance;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Azure implements IEdsConfigModel {
        private EdsAzureConfigModel.Cred cred;
        private EdsAzureConfigModel.Login login;
        private EdsInstance edsInstance;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Cloudflare implements IEdsConfigModel {
        @Schema(description = "凭据")
        private EdsCloudflareConfigModel.Cred cred;
        private EdsInstance edsInstance;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Cratos implements  IEdsConfigModel {
        private String version;
        private EdsInstance edsInstance;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Dingtalk implements IEdsConfigModel {
        private String version;
        private String url;
        private String company;
        private String corpId;
        private EdsDingtalkConfigModel.DingtalkApp app;
        private EdsInstance edsInstance;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Robot implements IEdsConfigModel {
        private String token;
        private String desc;
        private EdsInstance edsInstance;
    }

    @Data
    @NoArgsConstructor
    @Schema(description = "gandi.net")
    public static class Gandi implements HasDnsNameServers, IEdsConfigModel {
        @Schema(description = "凭据")
        private EdsGandiConfigModel.Cred cred;
        private List<String> nameServers;
        private EdsInstance edsInstance;
    }

    @Data
    @NoArgsConstructor
    public static class Gcp implements IEdsConfigModel {
        private EdsGcpConfigModel.Project project;
        private EdsInstance edsInstance;
        private EdsGcpConfigModel.Certificate certificate;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class GitLab implements IEdsConfigModel {
        private EdsGitLabConfigModel.Api api;
        private EdsGitLabConfigModel.SystemHooks systemHooks;
        private EdsGitLabConfigModel.GitFlow gitFlow;
        @Schema(description = "凭据")
        private EdsGitLabConfigModel.Cred cred;
        private EdsInstance edsInstance;
    }

    @Data
    @NoArgsConstructor
    @Schema(description = "godaddy.com")
    public static class Godaddy implements HasDnsNameServers, IEdsConfigModel {
        @Schema(description = "凭据")
        private EdsGodaddyConfigModel.Cred cred;
        private String customerId;
        private List<String> nameServers;
        private EdsInstance edsInstance;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Harbor implements IEdsConfigModel {
        private String version;
        private String url;
        @Schema(description = "凭据")
        private EdsHarborConfigModel.Cred cred;
        private EdsInstance edsInstance;

        public String acqUrl() {
            if (StringUtils.hasText(this.url)) {
                return url + API_V2;
            } else {
                return null;
            }
        }
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Hcs implements HasRegionsModel, IEdsConfigModel {
        private String version;
        // default
        private String regionId;
        private Set<String> regionIds;
        @Schema(description = "凭据")
        private EdsHcsConfigModel.Cred cred;
        private EdsHcsConfigModel.ManageOne manageOne;
        private EdsInstance edsInstance;
        private Boolean ignoreSSLVerification;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Hwc implements HasRegionsModel, IEdsConfigModel {
        private String version;
        // default
        private String regionId;
        private Set<String> regionIds;
        @Schema(description = "凭据")
        private EdsHwcConfigModel.Cred cred;
        private List<EdsHwcConfigModel.Project> projects;
        private EdsInstance edsInstance;
        private EdsHwcConfigModel.IAM iam;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Jenkins implements IEdsConfigModel {
        private String version;
        private String url;
        private String ip;
        private EdsJenkinsConfigModel.Cred cred;
        private EdsJenkinsConfigModel.Security security;
        private String name;
        private EdsInstance edsInstance;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Kubernetes implements IEdsConfigModel {
        private String version;
        private String provider;
        @Schema(description = "Amazon EKS cred")
        private EdsKubernetesConfigModel.AmazonEks amazonEks;
        private EdsKubernetesConfigModel.Kubeconfig kubeconfig;
        private EdsKubernetesConfigModel.Filter filter;
        private EdsInstance edsInstance;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Ldap implements IEdsConfigModel {
        private String url;
        private String base;
        private EdsLdapConfigModel.LdapManage manager; // 管理员账户
        private EdsLdapConfigModel.LdapUser user;
        private EdsLdapConfigModel.LdapGroup group;
        private EdsInstance edsInstance;

        public String buildUserDn(String username) {
            return Joiner.on(",")
                    .skipNulls()
                    .join(StringFormatter.arrayFormat(RDN, user.getId(), username), user.getDn());
        }

        public String buildGroupDn(String groupName) {
            return Joiner.on(",")
                    .skipNulls()
                    .join(StringFormatter.arrayFormat(RDN, group.getId(), groupName), group.getDn());
        }
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Opscloud implements IEdsConfigModel {
        private final String version = "4";
        private String url;
        @Schema(description = "凭据")
        private EdsOpscloudConfigModel.Cred cred;
        private EdsInstance edsInstance;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Zabbix implements IEdsConfigModel {
        private String version;
        private String url;
        @Schema(description = "凭据")
        private EdsZabbixConfigModel.Cred cred;
        private EdsZabbixConfigModel.Alert alert;
        private String region;
        private List<String> severityTypes;
        private EdsInstance edsInstance;
    }

    /**
     * EdsEagle
     */
    @Data
    @NoArgsConstructor
    @Schema
    public static class Sase implements IEdsConfigModel {
        private EdsEagleCloudConfigModel.Cred cred;
        // 安全管理员
        private List<EdsEagleCloudConfigModel.SecurityAdministrator> securityAdministrators;
        private EdsInstance edsInstance;
    }

}
