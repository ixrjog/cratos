package com.baiyi.cratos.domain.param.http.network;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.GlobalNetworkSubnet;
import com.baiyi.cratos.domain.param.HasImportFromAsset;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/26 10:36
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class GlobalNetworkSubnetParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class GlobalNetworkSubnetPageQuery extends PageParam implements BusinessTagParam.HasQueryByTag {
        @Schema(description = "查询名称")
        private String queryName;
        private BusinessTagParam.QueryByTag queryByTag;
        private List<Integer> idList;

        public GlobalNetworkSubnetPageQueryParam toParam() {
            return GlobalNetworkSubnetPageQueryParam.builder()
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
    public static class GlobalNetworkSubnetPageQueryParam extends PageParam {
        private String queryName;
        private List<Integer> idList;
    }

    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.GLOBAL_NETWORK_SUBNET)
    public static class AddGlobalNetworkSubnet implements IToTarget<GlobalNetworkSubnet>, HasImportFromAsset {
        private Integer id;
        private String name;
        private String mainName;
        private String mainType;
        private Integer mainId;
        private String subnetKey;
        private String region;
        private String zone;
        private String cidrBlock;
        private Integer resourceTotal;
        private Boolean valid;
        private String comment;
        @Schema(description = "Import from assetId")
        private Integer fromAssetId;
    }

    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.GLOBAL_NETWORK_SUBNET)
    public static class UpdateGlobalNetworkSubnet implements IToTarget<GlobalNetworkSubnet>, HasImportFromAsset {
        private Integer id;
        private String name;
        private String mainName;
        private String mainType;
        private Integer mainId;
        private String subnetKey;
        private String region;
        private String zone;
        private String cidrBlock;
        private Integer resourceTotal;
        private Boolean valid;
        private String comment;
        @Schema(description = "Import from assetId")
        private Integer fromAssetId;
    }

}
