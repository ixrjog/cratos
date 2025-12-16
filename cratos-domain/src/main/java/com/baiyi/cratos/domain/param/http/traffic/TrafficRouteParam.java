package com.baiyi.cratos.domain.param.http.traffic;

import com.baiyi.cratos.domain.generator.TrafficRecordTarget;
import com.baiyi.cratos.domain.generator.TrafficRoute;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/11 09:56
 * &#064;Version 1.0
 */
public class TrafficRouteParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class RoutePageQuery extends PageParam implements BusinessTagParam.HasQueryByTag {
        @Schema(description = "查询名称")
        private String queryName;
        private BusinessTagParam.QueryByTag queryByTag;
        private List<Integer> idList;

        public RoutePageQueryParam toParam() {
            return RoutePageQueryParam.builder()
                    .page(getPage())
                    .length(getLength())
                    .queryName(queryName)
                    .idList(idList)
                    .build();
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class RoutePageQueryParam extends PageParam {
        private String queryName;
        private List<Integer> idList;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class AddRoute implements IToTarget<TrafficRoute> {
        @Null(message = "ID must be null")
        private Integer id;
        @NotNull(message = "Domain ID cannot be null")
        private Integer domainId;
        @NotNull(message = "Domain record ID cannot be null")
        private Integer domainRecordId;
        @NotBlank(message = "Domain cannot be blank")
        private String domain;
        @NotBlank(message = "Domain record cannot be blank")
        private String domainRecord;
        private String name;
        @NotNull(message = "DNS resolver instance ID cannot be null")
        private Integer dnsResolverInstanceId;
        @Null(message = "ZoneID must be null")
        private String zoneId;
        @NotBlank(message = "Record type cannot be blank")
        private String recordType;
        @NotNull(message = "Valid status cannot be null")
        private Boolean valid;
        private String comment;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class UpdateRoute implements IToTarget<TrafficRoute> {
        @NotNull
        private Integer id;
        @NotNull(message = "Domain ID cannot be null")
        private Integer domainId;
        @NotNull(message = "Domain record ID cannot be null")
        private Integer domainRecordId;
        @NotBlank(message = "Domain cannot be blank")
        private String domain;
        @NotBlank(message = "Domain record cannot be blank")
        private String domainRecord;
        private String name;
        @NotNull(message = "DNS resolver instance ID cannot be null")
        private Integer dnsResolverInstanceId;
        @NotBlank(message = "Record type cannot be blank")
        private String recordType;
        @NotNull(message = "Valid status cannot be null")
        private Boolean valid;
        private String comment;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class AddRecordTarget implements IToTarget<TrafficRecordTarget> {
        @NotNull
        private Integer id;
        @NotNull(message = "Domain ID cannot be null")
        private Integer domainId;
        @NotNull(message = "Domain record ID cannot be null")
        private Integer domainRecordId;
        @NotBlank(message = "Domain cannot be blank")
        private String domain;
        @NotBlank(message = "Domain record cannot be blank")
        private String domainRecord;
        private String name;
        @NotNull(message = "DNS resolver instance ID cannot be null")
        private Integer dnsResolverInstanceId;
        @NotBlank(message = "Record type cannot be blank")
        private String recordType;
        @NotNull(message = "Valid status cannot be null")
        private Boolean valid;
        private String comment;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class SwitchRecordTarget {
        @NotNull
        private Integer recordTargetId;
        @NotBlank(message = "RoutingOptions cannot be blank")
        private String routingOptions;
    }

}
