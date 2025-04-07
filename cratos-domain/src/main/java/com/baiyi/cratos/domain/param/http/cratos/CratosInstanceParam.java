package com.baiyi.cratos.domain.param.http.cratos;

import com.baiyi.cratos.domain.param.PageParam;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/7 15:24
 * &#064;Version 1.0
 */
public class CratosInstanceParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class RegisteredInstancePageQuery extends PageParam implements BusinessTagParam.HasQueryByTag {
        @Schema(description = "查询名称")
        private String queryName;
        private BusinessTagParam.QueryByTag queryByTag;
        private Boolean valid;
        private List<Integer> idList;

        public RegisteredInstancePageQueryParam toParam() {
            return RegisteredInstancePageQueryParam.builder()
                    .page(getPage())
                    .length(getLength())
                    .queryName(queryName)
                    .idList(idList)
                    .valid(valid)
                    .build();
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class RegisteredInstancePageQueryParam extends PageParam {
        private String queryName;
        private List<Integer> idList;
        private Boolean valid;
    }


}
