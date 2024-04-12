package com.baiyi.cratos.domain.view.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/4/12 上午11:44
 * @Version 1.0
 */
public class RoleMenuVO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class RoleMenu {

        private List<Menu> items;

    }

    @Data
    @Schema
    public static class Menu {

        private Integer id;

        private String name;

        @Schema(description = "Name")
        private String title;

        private String lang;

        @Schema(description = "For FE")
        private Boolean open;

        @Schema(description = "For FE")
        private Boolean disabled;

        @Schema(description = "For FE")
        private Boolean isChecked;

        private String icon;

        private String link;

        private Integer seq;

        private Boolean valid;

        private String menuType;

        private Integer roleId;

        private List<Menu> children;

    }

}
