package com.baiyi.cratos.domain.param.menu;

import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Author baiyi
 * @Date 2024/4/7 下午3:32
 * @Version 1.0
 */
public class MenuParam {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class QueryMyMenu {

        private String lang;

    }

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class MenuPageQuery extends PageParam {

        private String queryName;

        @Schema(description = "MAIN, CHILDREN")
        private String menuType;

        private Integer parentId;

        private String lang;

    }

}
