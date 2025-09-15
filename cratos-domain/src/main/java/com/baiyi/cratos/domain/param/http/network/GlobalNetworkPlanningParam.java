package com.baiyi.cratos.domain.param.http.network;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.GlobalNetworkPlanning;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/2 16:22
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class GlobalNetworkPlanningParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class GlobalNetworkPlanningPageQuery extends PageParam implements BusinessTagParam.HasQueryByTag {
        @Schema(description = "查询名称")
        private String queryName;
        private Integer networkId;
        private BusinessTagParam.QueryByTag queryByTag;
        private List<Integer> idList;

        public GlobalNetworkPlanningParam.GlobalNetworkPlanningPageQueryParam toParam() {
            return GlobalNetworkPlanningParam.GlobalNetworkPlanningPageQueryParam.builder()
                    .page(getPage())
                    .length(getLength())
                    .queryName(queryName)
                    .networkId(networkId)
                    .idList(idList)
                    .build();
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class GlobalNetworkPlanningPageQueryParam extends PageParam {
        private String queryName;
        private Integer networkId;
        private List<Integer> idList;
    }

    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.GLOBAL_NETWORK_SUBNET)
    public static class AddGlobalNetworkPlanning  implements IToTarget<GlobalNetworkPlanning> {
        @Null
        private Integer id;
        @NotNull
        private Integer networkId;
        @NotBlank
        private String name;
        @NotBlank
        private String cidrBlock;
        private Integer resourceTotal;
        @NotNull
        private Boolean valid;
        private String comment;
    }

    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.GLOBAL_NETWORK_SUBNET)
    public static class UpdateGlobalNetworkPlanning  implements IToTarget<GlobalNetworkPlanning> {
        @NotNull
        private Integer id;
        @NotNull
        private Integer networkId;
        @NotBlank
        private String name;
        @NotBlank
        private String cidrBlock;
        private Integer resourceTotal;
        @NotNull
        private Boolean valid;
        private String comment;
    }

}
