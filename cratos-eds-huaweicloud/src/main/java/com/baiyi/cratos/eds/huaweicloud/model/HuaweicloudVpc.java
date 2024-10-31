package com.baiyi.cratos.eds.huaweicloud.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/10/31 10:33
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class HuaweicloudVpc {

    public static Vpc toVpc(String regionId, String projectId, com.huaweicloud.sdk.vpc.v2.model.Vpc vpc) {
        VpcModel vpcModel = VpcModel.builder()
                .id(vpc.getId())
                .name(vpc.getName())
                .description(vpc.getDescription())
                .cidr(vpc.getCidr())
                .status(vpc.getStatus()
                        .getValue())
                .projectId(projectId)
                .enterpriseProjectId(vpc.getEnterpriseProjectId())
                .createdAt(Date.from(vpc.getCreatedAt()
                        .toInstant()))
                .updatedAt(Date.from(vpc.getUpdatedAt()
                        .toInstant()))
                .build();
        return Vpc.builder()
                .regionId(regionId)
                .vpc(vpcModel)
                .build();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Vpc {
        private String regionId;
        private VpcModel vpc;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VpcModel {
        private String id;
        private String name;
        private String description;
        private String cidr;
        private List<String> extendCidrs;
        private String status;
        private String projectId;
        private String enterpriseProjectId;
        private Date createdAt;
        private Date updatedAt;
        private List<CloudResource> cloudResources;
        // private List<Tag> tags = null;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CloudResource {
        private String resourceType;
        private Integer resourceCount;
    }

}
