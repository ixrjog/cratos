package com.baiyi.cratos.domain.param.traffic;

import com.baiyi.cratos.domain.generator.TrafficLayerDomain;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import com.baiyi.cratos.domain.param.tag.BusinessTagParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/3/29 11:42
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class TrafficLayerDomainParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class DomainPageQuery extends PageParam implements BusinessTagParam.HasQueryByTag {
        @Schema(description = "查询名称")
        private String queryName;
        private BusinessTagParam.QueryByTag queryByTag;
        private List<Integer> idList;

        public DomainPageQueryParam toParam() {
            return DomainPageQueryParam.builder()
                    .page(getPage())
                    .length(getLength())
                    .queryName(queryName)
                    .idList(idList)
                    .build();
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class DomainPageQueryParam extends PageParam {
        private String queryName;
        private List<Integer> idList;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class AddDomain implements IToTarget<TrafficLayerDomain> {
        private String name;
        private String domain;
        private Boolean valid;
        private String comment;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class UpdateDomain implements IToTarget<TrafficLayerDomain> {
        private Integer id;
        private String name;
        private String domain;
        private Boolean valid;
        private String comment;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class QueryDomainEnv {
        private Integer domainId;
    }

}
