package com.baiyi.cratos.domain.view.eds;

import com.baiyi.cratos.domain.view.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author baiyi
 * @Date 2024/2/28 14:53
 * @Version 1.0
 */
public class EdsAssetVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Asset extends BaseVO implements Serializable {

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

    }


}
