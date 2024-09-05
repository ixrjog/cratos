package com.baiyi.cratos.domain.view.network;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.doc.BusinessDocVO;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/26 11:08
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class GlobalNetworkVO {

    public interface HasNetwork {
        Integer getNetworkId();

        void setNetwork(Network network);
    }

    public interface HasPlannings {
        Integer getNetworkId();

        void setPlannings(List<Planning> plannings);
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.GLOBAL_NETWORK)
    public static class Network extends BaseVO implements BaseBusiness.IBusinessAnnotate, BusinessTagVO.HasBusinessTags, BusinessDocVO.HasBusinessDocs, HasPlannings, Serializable {
        @Serial
        private static final long serialVersionUID = -7115650253857394579L;
        private Integer id;
        private String name;
        private String mainName;
        private String cidrBlock;
        private Integer resourceTotal;
        private Boolean valid;
        private String comment;
        private List<Planning> plannings;

        @Override
        public Integer getBusinessId() {
            return id;
        }

        @Schema(description = "Business Tags")
        private List<BusinessTagVO.BusinessTag> businessTags;
        @Schema(description = "Business Docs")
        private List<BusinessDocVO.BusinessDoc> businessDocs;

        @Override
        public Integer getNetworkId() {
            return id;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.GLOBAL_NETWORK_PLANNING)
    public static class Planning extends BaseVO implements BaseBusiness.IBusinessAnnotate, BusinessTagVO.HasBusinessTags, BusinessDocVO.HasBusinessDocs, Serializable {
        @Serial
        private static final long serialVersionUID = -8446398754921903111L;
        private Integer id;
        private Integer networkId;
        private String name;
        private String cidrBlock;
        private Integer resourceTotal;
        private Boolean valid;
        private String comment;

        private Network network;

        @Override
        public Integer getBusinessId() {
            return id;
        }

        @Schema(description = "Business Tags")
        private List<BusinessTagVO.BusinessTag> businessTags;
        @Schema(description = "Business Docs")
        private List<BusinessDocVO.BusinessDoc> businessDocs;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.GLOBAL_NETWORK_SUBNET)
    public static class Subnet extends BaseVO implements BaseBusiness.IBusinessAnnotate, BusinessTagVO.HasBusinessTags, BusinessDocVO.HasBusinessDocs, Serializable {
        @Serial
        private static final long serialVersionUID = 7872000380929970531L;
        private Integer id;
        private String name;
        private String mainName;
        private String mainType;
        private Integer mainId;
        private String cidrBlock;
        private Integer resourceTotal;
        private Boolean valid;
        private String comment;

        @Override
        public Integer getBusinessId() {
            return id;
        }

        @Schema(description = "Business Tags")
        private List<BusinessTagVO.BusinessTag> businessTags;
        @Schema(description = "Business Docs")
        private List<BusinessDocVO.BusinessDoc> businessDocs;
    }

}
