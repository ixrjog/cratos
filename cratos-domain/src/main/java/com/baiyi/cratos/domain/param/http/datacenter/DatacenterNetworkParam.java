package com.baiyi.cratos.domain.param.http.datacenter;

import com.baiyi.cratos.domain.generator.DatacenterNetwork;
import com.baiyi.cratos.domain.generator.DatacenterNetworkAllocation;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

public class DatacenterNetworkParam {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class NetworkPageQuery extends PageParam {
        private String queryName;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class AddNetwork implements IToTarget<DatacenterNetwork> {
        private String name;
        private Integer accountEntityId;
        private String datacenterType;
        private Integer edsInstanceId;
        private String comment;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class UpdateNetwork implements IToTarget<DatacenterNetwork> {
        private Integer id;
        private String name;
        private Integer accountEntityId;
        private String datacenterType;
        private Integer edsInstanceId;
        private String comment;
        private Boolean valid;
    }

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AllocationPageQuery extends PageParam {
        private Integer networkId;
        private String queryName;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class AddAllocation implements IToTarget<DatacenterNetworkAllocation> {
        private Integer networkId;
        private String name;
        private String region;
        private String cidr;
        private String allocationType;
        private Boolean allowOverlap;
        private String nat;
        private String comment;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class UpdateAllocation implements IToTarget<DatacenterNetworkAllocation> {
        private Integer id;
        private Integer networkId;
        private String name;
        private String region;
        private String cidr;
        private String allocationType;
        private Boolean allowOverlap;
        private String nat;
        private Boolean valid;
        private String comment;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class CheckCidrConflict {
        @Schema(description = "CIDR to check")
        private String cidr;
        @Schema(description = "Exclude allocation ID (for edit)")
        private Integer excludeId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class FindAvailableCidr {
        @Schema(description = "Private address range, e.g. 10.0.0.0/8, 172.16.0.0/12")
        private String parentCidr;
        @Schema(description = "Subnet prefix length, e.g. 24")
        private int prefixLength;
        @Schema(description = "Max results to return")
        @Builder.Default
        private int limit = 10;
    }

}
