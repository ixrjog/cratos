package com.baiyi.cratos.domain.view.traffic;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.HasResourceCount;
import com.baiyi.cratos.domain.view.doc.BusinessDocVO;
import com.baiyi.cratos.domain.view.env.EnvVO;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/3/29 11:32
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class TrafficLayerDomainVO {

    public interface HasDomain {
        Integer getDomainId();

        void setDomain(Domain domain);
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    @BusinessType(type = BusinessTypeEnum.TRAFFIC_LAYER_DOMAIN)
    public static class Domain extends BaseVO implements HasResourceCount, BaseBusiness.IBusinessAnnotate, BusinessTagVO.HasBusinessTags, BusinessDocVO.HasBusinessDocs, Serializable {
        @Serial
        private static final long serialVersionUID = -8990726488134957647L;
        private Integer id;
        private String name;
        private String domain;
        private String registeredDomain;
        private List<DomainNameServiceProvider> dnsProviders;
        private Boolean valid;
        private String comment;
        private Map<String, Integer> resourceCount;

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
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class DomainEnv extends EnvVO.Env implements Serializable {
        @Serial
        private static final long serialVersionUID = 4193131699641687394L;
        private String envName;
        private Boolean valid;
        private Integer seq;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class DomainNameServiceProvider implements Serializable {
        @Serial
        private static final long serialVersionUID = -3458222164485918824L;
        private String providerType;
        private String providerName;
        private String consoleUrl;
    }

}
