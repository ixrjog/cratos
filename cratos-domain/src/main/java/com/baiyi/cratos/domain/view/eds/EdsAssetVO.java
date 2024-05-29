package com.baiyi.cratos.domain.view.eds;

import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.HasResourceCount;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2024/2/28 14:53
 * @Version 1.0
 */
public class EdsAssetVO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class ToBusiness implements Serializable {

        @Serial
        private static final long serialVersionUID = 8963852386777447343L;

        private String businessType;

        private Integer businessId;

        private Integer assetId;

        @Schema(description = "已绑定")
        private Boolean bind;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AssetToBusiness<T extends IToBusinessTarget> implements Serializable {

        @Serial
        private static final long serialVersionUID = -1350874228317416818L;

        private ToBusiness toBusiness;

        T target;

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Asset extends BaseVO implements HasResourceCount, Serializable {

        @Serial
        private static final long serialVersionUID = 4604127025098701159L;

        private Integer id;

        private Integer parentId;

        private Integer instanceId;

        private String name;

        private String assetId;

        private String assetKey;

        private String assetType;

        private String kind;

        private String version;

        private Boolean valid;

        private String region;

        private String zone;

        private String assetStatus;

        private Date createdTime;

        private Date expiredTime;

        private String originalModel;

        private Object originalAsset;

        private String description;

        private ToBusiness toBusiness;

        private Map<String, Integer> resourceCount;

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Index extends BaseVO implements Serializable {

        @Serial
        private static final long serialVersionUID = -7612746160295687836L;

        private Integer id;

        private Integer instanceId;

        private Integer assetId;

        private String name;

        private String value;

        private String comment;

    }

}
