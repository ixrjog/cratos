package com.baiyi.cratos.domain.param.menu;

import com.baiyi.cratos.domain.generator.Menu;
import com.baiyi.cratos.domain.generator.MenuTitle;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/4/7 下午3:32
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
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
    public static class UpdateMenu implements IToTarget<Menu>, IToTitles {

        private Integer id;

        @NotBlank
        private String name;

        private String icon;

        @NotBlank
        private String link;

        @NotNull
        private Integer seq;

        @NotNull
        private Integer parentId;

        @NotNull
        private Boolean valid;

        @NotBlank
        private String menuType;

        private List<Title> titles;

    }

    public interface IToTitles {

        List<Title> getTitles();

        default List<MenuTitle> toTitles() {
            return getTitles().stream()
                    .map(IToTarget::toTarget)
                    .toList();
        }

    }

    @Data
    @Schema
    public static class AddMenu implements IToTarget<Menu>, IToTitles {

        @NotBlank
        private String name;

        private String icon;

        @NotBlank
        private String link;

        @NotNull
        private Integer seq;

        @NotNull
        private Integer parentId;

        @NotNull
        private Boolean valid;

        @NotBlank
        private String menuType;

        private List<Title> titles;

    }

    @Data
    @Schema
    public static class Title implements IToTarget<MenuTitle> {

        private Integer menuId;

        private String title;

        private String lang;

        private Boolean preference;

    }

}
