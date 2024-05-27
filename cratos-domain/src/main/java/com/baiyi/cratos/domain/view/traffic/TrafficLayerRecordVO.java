package com.baiyi.cratos.domain.view.traffic;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.CachedVO;
import com.baiyi.cratos.domain.view.doc.BusinessDocVO;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.env.EnvVO;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2024/3/29 11:34
 * @Version 1.0
 */
public class TrafficLayerRecordVO {

    public interface IRecord {

        Integer getRecordId();

        void setRecord(Record record);

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    @BusinessType(type = BusinessTypeEnum.TRAFFIC_LAYER_RECORD)
    public static class Record extends BaseVO implements TrafficLayerDomainVO.IDomain, EnvVO.HasEnv, BaseBusiness.IBusinessAnnotate, BusinessTagVO.IBusinessTags, BusinessDocVO.HasBusinessDocs, Serializable {

        @Serial
        private static final long serialVersionUID = -6173359438855341290L;

        private Integer id;

        private Integer domainId;

        private String envName;

        private String recordName;

        private String routeTrafficTo;

        private String originServer;

        private Boolean valid;

        private String comment;

        private TrafficLayerDomainVO.Domain domain;

        @Override
        public Integer getBusinessId() {
            return id;
        }

        @Schema(description = "Business Tags")
        List<BusinessTagVO.BusinessTag> businessTags;

        @Schema(description = "Business Docs")
        List<BusinessDocVO.BusinessDoc> businessDocs;

        private EnvVO.Env env;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class RecordDetails implements IRecord, Serializable {

        public static final RecordDetails NOT_FOUND = RecordDetails.builder()
                .build();

        @Serial
        private static final long serialVersionUID = 4049706572702022792L;

        private Integer recordId;

        private Record record;

        private OriginServer originServer;

        private TableDetails tableDetails;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class TableDetails implements Serializable {

        @Serial
        private static final long serialVersionUID = -4666058159156691234L;

        private String recordTable;

        private String lbTable;

        private String ingressRuleTable;
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class OriginServer implements CachedVO.ICached, Serializable {

        @Serial
        private static final long serialVersionUID = -5858488088287315080L;

        private List<EdsAssetVO.Asset> origins;

        /**
         * Keys: RULES / HOSTNAME
         */
        private Map<String, List<EdsAssetVO.Index>> details;

        @Builder.Default
        private CachedVO.Cached cached = CachedVO.Cached.builder()
                .build();

    }

}
