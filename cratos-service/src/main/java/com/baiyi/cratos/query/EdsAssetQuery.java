package com.baiyi.cratos.query;

import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.param.PageParam;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/7 11:25
 * &#064;Version 1.0
 */
public class EdsAssetQuery {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QueryUserPermissionBusinessIdParam implements Serializable {
        @Serial
        private static final long serialVersionUID = 3186486956925666726L;
        private String username;
        private List<String> groups;
        private Integer tagGroupId;
        private final String businessType = BusinessTypeEnum.EDS_ASSET.name();
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserPermissionPageQueryParam extends PageParam implements Serializable {
        @Serial
        private static final long serialVersionUID = -1737675959336300259L;
        private String username;
        private List<Integer> userPermissionIds;
        private List<String> effectiveAssetTypes;
        private String queryName;
        private final String businessType = BusinessTypeEnum.EDS_ASSET.name();
    }

}
