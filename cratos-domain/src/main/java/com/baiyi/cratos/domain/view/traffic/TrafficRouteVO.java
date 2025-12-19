package com.baiyi.cratos.domain.view.traffic;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.HasEdsInstance;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.TrafficRoute;
import com.baiyi.cratos.domain.model.DNS;
import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.HasResourceCount;
import com.baiyi.cratos.domain.view.doc.BusinessDocVO;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.domain.view.env.EnvVO;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/11 10:16
 * &#064;Version 1.0
 */
public class TrafficRouteVO {

    public interface HasRecordTargets {
        Integer getTrafficRouteId();

        DNS.ResourceRecordSet getDnsResourceRecordSet();

        TrafficRoute toTrafficRoute();

        void setRecordTargets(List<RecordTarget> recordTargets);
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    @BusinessType(type = BusinessTypeEnum.TRAFFIC_ROUTE)
    public static class Route extends BaseVO implements HasResourceCount, BaseBusiness.IBusinessAnnotate, BusinessTagVO.HasBusinessTags, BusinessDocVO.HasBusinessDocs, EnvVO.HasEnv, HasRecordTargets, HasEdsInstance, Serializable {
        @Serial
        private static final long serialVersionUID = 7924531542599490285L;
        private Integer id;
        private Integer domainId;
        private Integer domainRecordId;
        private String domain;
        private String domainRecord;
        private String name;
        private Integer dnsResolverInstanceId;
        private String recordType;
        private Boolean valid;
        private String comment;
        private String zoneId;

        public TrafficRoute toTrafficRoute() {
            return TrafficRoute.builder()
                    .id(id)
                    .domainId(domainId)
                    .domainRecordId(domainRecordId)
                    .domain(domain)
                    .domainRecord(domainRecord)
                    .name(name)
                    .dnsResolverInstanceId(dnsResolverInstanceId)
                    .recordType(recordType)
                    .zoneId(zoneId)
                    .valid(valid)
                    .comment(comment)
                    .build();
        }

        @Override
        public Integer getBusinessId() {
            return id;
        }

        private DNS.ResourceRecordSet dnsResourceRecordSet;

        private String envName;
        private EnvVO.Env env;
        private Map<String, Integer> resourceCount;
        @Schema(description = "Business Tags")
        private List<BusinessTagVO.BusinessTag> businessTags;
        @Schema(description = "Business Docs")
        private List<BusinessDocVO.BusinessDoc> businessDocs;

        @Override
        public Integer getTrafficRouteId() {
            return id;
        }

        private List<RecordTarget> recordTargets;
        private EdsInstanceVO.EdsInstance dnsResolverInstance;

        @Override
        public void setEdsInstance(EdsInstanceVO.EdsInstance edsInstance) {
            this.dnsResolverInstance = edsInstance;
        }

        @Override
        public Integer getInstanceId() {
            return dnsResolverInstanceId;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    @BusinessType(type = BusinessTypeEnum.TRAFFIC_RECORD_TARGET)
    public static class RecordTarget extends BaseVO implements BaseBusiness.IBusinessAnnotate, BusinessTagVO.HasBusinessTags, BusinessDocVO.HasBusinessDocs, Serializable {
        @Serial
        private static final long serialVersionUID = -4707021759528048777L;

        private Integer id;
        private Integer trafficRouteId;
        private String recordType;
        private String resourceRecord;
        private String recordValue;
        private String targetType;
        private Boolean origin;
        private Long ttl;
        private Integer weight;
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

        @Schema(description = "DNS指向当前目标")
        @Builder.Default
        private boolean isActive = false;
        private DNS.ResourceRecord dnsResourceRecord;

        public void setDnsResourceRecord(DNS.ResourceRecord dnsResourceRecord) {
            this.dnsResourceRecord = dnsResourceRecord;
            this.isActive = true;
        }
    }

}
