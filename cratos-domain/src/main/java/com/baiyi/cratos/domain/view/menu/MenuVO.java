package com.baiyi.cratos.domain.view.menu;

import com.baiyi.cratos.domain.view.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/4/7 下午3:21
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class MenuVO {

    public interface HasMenuChildren {
        Integer getMenuId();
        String getLang();
        void setChildren(List<Menu> children);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class NavMenu {
        private List<Menu> items;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    public static class Menu extends BaseVO implements HasMenuChildren {
        @Serial
        private static final long serialVersionUID = 1162467323072243868L;
        private Integer id;
        private String name;
        @Schema(description = "Name")
        private String title;
        private String lang;
        private String icon;
        private String link;
        private Integer seq;
        private Integer parentId;
        private Boolean valid;
        private String menuType;
        private List<Menu> children;
        private List<Title> menuTitles;
        @Schema(description = "For FE")
        private Boolean active;
        @Schema(description = "For FE")
        private Boolean open;
        @Override
        public Integer getMenuId() {
            return id;
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    public static class Title extends BaseVO {
        @Serial
        private static final long serialVersionUID = 2153183552886990495L;
        private Integer id;
        private Integer menuId;
        private String title;
        private String lang;
        private Boolean preference;
    }

}
