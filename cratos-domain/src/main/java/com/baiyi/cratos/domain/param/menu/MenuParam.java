package com.baiyi.cratos.domain.param.menu;

import com.baiyi.cratos.domain.generator.Menu;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
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
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class QueryUserMenu {

        private String username;

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

        @Schema(description = "MAIN, SUB")
        private String menuType;

        private Integer parentId;

        private String lang;

    }

    @Data
    @Schema
    public static class UpdateMenu implements IToTarget<Menu> {

        private Integer id;

        @NotBlank
        private String name;

        private String icon;

        @NotBlank
        private String link;

        @NonNull
        private Integer seq;

        @NonNull
        private Integer parentId;

        @NonNull
        private Boolean valid;

        @NotBlank
        private String menuType;

    }

    @Data
    @Schema
    public static class AddMenu implements IToTarget<Menu> {

        @NotBlank
        private String name;

        private String icon;

        @NotBlank
        private String link;

        @NonNull
        private Integer seq;

        @NonNull
        private Integer parentId;

        @NonNull
        private Boolean valid;

        @NotBlank
        private String menuType;

    }

}
