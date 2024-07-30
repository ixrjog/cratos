package com.baiyi.cratos.domain.param.asset;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.AssetMaturity;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/30 下午2:24
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AssetMaturityParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class AssetMaturityPageQuery extends PageParam {
        @Schema(description = "Query by name")
        private String queryName;
    }

    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.ASSET_MATURITY)
    public static class AddAssetMaturity implements IToTarget<AssetMaturity> {
        @NotBlank
        private String name;
        @NotNull
        private String itemType;
        @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date subscriptionTime;
        @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd HH:mm:ss")
        @NotNull
        private Date expiry;
        @NotNull
        private Boolean valid;
        @NotNull
        private Boolean autoRenewal;
        private String comment;
    }

    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.ASSET_MATURITY)
    public static class UpdateAssetMaturity implements IToTarget<AssetMaturity> {
        @NotNull
        private Integer id;
        @NotBlank
        private String name;
        @NotNull
        private String itemType;
        @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date subscriptionTime;
        @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd HH:mm:ss")
        @NotNull
        private Date expiry;
        @NotNull
        private Boolean valid;
        @NotNull
        private Boolean autoRenewal;
        private String comment;
    }

}