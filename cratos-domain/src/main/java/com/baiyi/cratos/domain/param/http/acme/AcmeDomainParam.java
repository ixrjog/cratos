package com.baiyi.cratos.domain.param.http.acme;

import com.baiyi.cratos.domain.generator.AcmeDomain;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/12 10:25
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AcmeDomainParam {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class AddDomain implements IToTarget<AcmeDomain> {
        private String name;
        private Integer domainId;
        private String domain;
        private String domains;
        private String zoneId;
        private Integer dnsResolverInstanceId;
        private Integer accountId;
        private String dcvType;
        private String dcvDelegationTarget;
        private Boolean valid;
        private String comment;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class UpdateDomain implements IToTarget<AcmeDomain> {
        @NotNull
        private Integer id;
        private String name;
        private Integer domainId;
        private String domain;
        private String domains;
        private String zoneId;
        private Integer dnsResolverInstanceId;
        private Integer accountId;
        private String dcvType;
        private String dcvDelegationTarget;
        private Boolean valid;
        private String comment;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class DomainPageQuery extends PageParam {
        private String queryName;
    }

}
