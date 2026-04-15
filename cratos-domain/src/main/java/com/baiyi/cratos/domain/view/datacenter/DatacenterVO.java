package com.baiyi.cratos.domain.view.datacenter;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.HasEdsInstance;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.account.AccountEntityVO;
import com.baiyi.cratos.domain.view.doc.BusinessDocVO;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/8 17:17
 * &#064;Version 1.0
 */
public class DatacenterVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.DATACENTER_NETWORK)
    public static class Network extends BaseVO implements AccountEntityVO.HasAccountEntity, HasEdsInstance, HasNetworkAllocations, BaseBusiness.IBusinessAnnotate, BusinessTagVO.HasBusinessTags, BusinessDocVO.HasBusinessDocs, Serializable {
        @Serial
        private static final long serialVersionUID = 8696116659836754036L;
        private Integer id;
        private String name;
        private Integer accountEntityId;
        private String datacenterType;
        private Integer edsInstanceId;
        private Boolean valid;
        private String comment;

        @Override
        public Integer getBusinessId() {
            return id;
        }

        @Override
        public Integer getNetworkId() {
            return id;
        }

        private EdsInstanceVO.EdsInstance edsInstance;
        private List<Allocation> allocations;
        private AccountEntityVO.AccountEntity accountEntity;

        @Schema(description = "Business Tags")
        private List<BusinessTagVO.BusinessTag> businessTags;
        @Schema(description = "Business Docs")
        private List<BusinessDocVO.BusinessDoc> businessDocs;

        @Override
        public Integer getInstanceId() {
            return edsInstanceId;
        }
    }

    public interface HasNetworkAllocations {
        Integer getNetworkId();

        void setAllocations(List<Allocation> allocations);
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.DATACENTER_NETWORK_ALLOCATION)
    public static class Allocation extends BaseVO implements BaseBusiness.IBusinessAnnotate, Serializable {
        @Serial
        private static final long serialVersionUID = 8696116659836754036L;
        private Integer id;
        private Integer networkId;
        private String name;
        private String region;
        private String cidr;
        private Long ipStart;
        private Long ipEnd;
        private String allocationType;
        private Boolean allowOverlap;
        private String nat;
        private Boolean valid;
        private String comment;

        private String networkName;

        @Override
        public Integer getBusinessId() {
            return id;
        }

        @Schema(description = "Business Tags")
        private List<BusinessTagVO.BusinessTag> businessTags;
        @Schema(description = "Business Docs")
        private List<BusinessDocVO.BusinessDoc> businessDocs;
    }

    @Data
    @Schema
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class CidrConflictResult implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        @Schema(description = "是否冲突")
        private boolean conflict;
        @Schema(description = "冲突的分配列表")
        private List<Allocation> conflictAllocations;
    }

    @Data
    @Schema
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class AvailableCidrResult implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        private String parentCidr;
        private int prefixLength;
        private List<String> availableCidrs;
    }

    @Data
    @Schema
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class SubnetBlock implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        private String cidr;
        private boolean allocated;
        private String allocationName;
        private String allocationType;
        private List<String> allocationTypes;
    }

    @Data
    @Schema
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class SubnetMap implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        private String parentCidr;
        private int prefixLength;
        private int cols;
        private int rows;
        private List<SubnetBlock> blocks;
    }

}
