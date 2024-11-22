package com.baiyi.cratos.domain.param.http.network;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.GlobalNetwork;
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
 * &#064;Date  2024/9/2 17:31
 * &#064;Version 1.0
 */
public class GlobalNetworkParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class GlobalNetworkPageQuery extends PageParam implements BusinessTagParam.HasQueryByTag {
        @Schema(description = "查询名称")
        private String queryName;
        private BusinessTagParam.QueryByTag queryByTag;
        private List<Integer> idList;

        public GlobalNetworkParam.GlobalNetworkPageQueryParam toParam() {
            return GlobalNetworkParam.GlobalNetworkPageQueryParam.builder()
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
    public static class GlobalNetworkPageQueryParam extends PageParam {
        private String queryName;
        private List<Integer> idList;
    }

    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.GLOBAL_NETWORK)
    public static class AddGlobalNetwork implements IToTarget<GlobalNetwork> {
        @Null
        private Integer id;
        @NotBlank
        private String name;
        @NotBlank
        private String mainName;
        @NotBlank
        private String cidrBlock;
        private Integer resourceTotal;
        private Boolean valid;
        private String comment;
    }

    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.GLOBAL_NETWORK)
    public static class UpdateGlobalNetwork implements IToTarget<GlobalNetwork> {
        @NotNull
        private Integer id;
        @NotBlank
        private String name;
        @NotBlank
        private String mainName;
        @NotBlank
        private String cidrBlock;
        private Integer resourceTotal;
        private Boolean valid;
        private String comment;
    }

    @Data
    @Schema
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QueryGlobalNetworkDetails {
        @NotNull
        private Integer id;
    }

}
