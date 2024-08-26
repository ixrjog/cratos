package com.baiyi.cratos.domain.param.network;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.GlobalNetwork;
import com.baiyi.cratos.domain.param.IImportFromAsset;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import com.baiyi.cratos.domain.param.tag.BusinessTagParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/26 10:36
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

        public GlobalNetworkPageQueryParam toParam() {
            return GlobalNetworkPageQueryParam.builder()
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
    public static class AddGlobalNetwork  implements IToTarget<GlobalNetwork>, IImportFromAsset {
        private Integer id;
        private String name;
        private String mainName;
        private String mainType;
        private Integer mainId;
        private String cidrBlock;
        private Integer resourceTotal;
        private Boolean valid;
        private String comment;
        @Schema(description = "Import from assetId")
        private Integer fromAssetId;
    }

    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.GLOBAL_NETWORK)
    public static class UpdateGlobalNetwork implements IToTarget<GlobalNetwork>, IImportFromAsset {
        private Integer id;
        private String name;
        private String mainName;
        private String mainType;
        private Integer mainId;
        private String cidrBlock;
        private Integer resourceTotal;
        private Boolean valid;
        private String comment;
        @Schema(description = "Import from assetId")
        private Integer fromAssetId;
    }

}
