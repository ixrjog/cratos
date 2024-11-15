package com.baiyi.cratos.domain.param.application;

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
 * &#064;Author  baiyi
 * &#064;Date  2024/5/20 上午10:49
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class ApplicationParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class ApplicationPageQuery extends PageParam implements BusinessTagParam.HasQueryByTag {
        @Schema(description = "查询名称")
        private String queryName;
        private BusinessTagParam.QueryByTag queryByTag;
        private List<Integer> idList;

        public ApplicationPageQueryParam toParam() {
            return ApplicationPageQueryParam.builder()
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
    public static class ApplicationPageQueryParam extends PageParam {
        private String queryName;
        private List<Integer> idList;
    }

}
