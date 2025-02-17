package com.baiyi.cratos.domain.view.eds;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.HasResourceCount;
import com.baiyi.cratos.domain.view.ToBusinessTarget;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/2/28 14:53
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
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
    public static class AssetToBusiness<T extends ToBusinessTarget> implements Serializable {
        @Serial
        private static final long serialVersionUID = -1350874228317416818L;
        private ToBusiness toBusiness;
        private T target;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    @BusinessType(type = BusinessTypeEnum.EDS_ASSET)
    public static class Asset extends BaseVO implements BaseBusiness.IBusinessAnnotate, BusinessTagVO.HasBusinessTags, HasResourceCount, Serializable {
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

        @Override
        public Integer getBusinessId() {
            return id;
        }

        @Schema(description = "Business Tags")
        private List<BusinessTagVO.BusinessTag> businessTags;
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

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class CloudIdentityDetails implements Serializable {
        @Serial
        private static final long serialVersionUID = 8165953149227317860L;
        public static final CloudIdentityDetails NO_DATA = CloudIdentityDetails.builder()
                .build();
        private String username;
        @Builder.Default
        private Map<String, Map<Integer, List<Asset>>> cloudIdentities = Map.of();
        @Builder.Default
        private Map<Integer, EdsInstanceVO.EdsInstance> instanceMap = Map.of();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class LdapIdentityDetails implements Serializable {
        public static final LdapIdentityDetails NO_DATA = LdapIdentityDetails.builder()
                .build();
        @Serial
        private static final long serialVersionUID = 6429767864454283221L;
        private String username;
        @Builder.Default
        private Map<Integer, Asset> ldapIdentities = Map.of();
        /**
         * Map<InstanceId, EdsInstanceVO.EdsInstance>
         */
        @Builder.Default
        private Map<Integer, EdsInstanceVO.EdsInstance> instanceMap = Map.of();
        /**
         * Map<AssetId, List<GroupName>>
         */
        @Builder.Default
        private Map<Integer, List<String>> ldapGroupMap = Map.of();
    }

}
