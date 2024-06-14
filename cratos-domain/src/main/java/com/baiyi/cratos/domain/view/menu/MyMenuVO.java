package com.baiyi.cratos.domain.view.menu;

import com.baiyi.cratos.domain.view.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.Collections;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/4/8 上午11:10
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class MyMenuVO {

    public interface HasMyMenuChildren {

        Integer getMenuId();

        String getLang();

        void setChildren(List<MyMenuVO.MyMenu> children);

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    public static class MyMenu extends BaseVO implements HasMyMenuChildren {

        public static final List<MyMenu> INVALID = Collections.emptyList();

        @Serial
        private static final long serialVersionUID = 1162467323072243868L;

        private Integer id;

        private String name;

        private String title;

        private String lang;

        private String icon;

        private String link;

        private Integer seq;

        private Integer parentId;

        private Boolean valid;

        private String menuType;

        private List<MyMenuVO.MyMenu> children;

        @Override
        public Integer getMenuId() {
            return id;
        }

    }

}
