package com.baiyi.cratos.domain.view.traffic;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2024/3/29 11:34
 * @Version 1.0
 */
public class TrafficLayerDomainRecordVO {

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
    @BusinessType(type = BusinessTypeEnum.TRAFFIC_LAYER_DOMAIN_RECORD)
    public static class Record extends BaseVO implements BaseBusiness.IBusinessAnnotate, BusinessTagVO.IBusinessTags, Serializable {

        @Serial
        private static final long serialVersionUID = -6173359438855341290L;

        private Integer id;

        private Integer domainId;

        private String envName;

        private String recordName;

        private String routeTrafficTo;

        private String originServer;

        private Boolean valid;

        private Date createTime;

        private Date updateTime;

        private String comment;

        @Override
        public Integer getBusinessId() {
            return id;
        }

        List<BusinessTagVO.BusinessTag> businessTags;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class RecordDetails implements IRecord, Serializable {

        @Serial
        private static final long serialVersionUID = 4049706572702022792L;

        private Integer recordId;

        private Record record;

        private OriginServer originServer;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class OriginServer implements Serializable {

        @Serial
        private static final long serialVersionUID = -5858488088287315080L;

        private List<EdsAssetVO.Asset> origins;

        /**
         * Keys: RULES / HOSTNAME
         */
        private Map<String, List<EdsAssetVO.Index>> details;

    }

}
