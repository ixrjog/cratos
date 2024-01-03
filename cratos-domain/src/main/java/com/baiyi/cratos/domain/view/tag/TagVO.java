package com.baiyi.cratos.domain.view.tag;

import com.baiyi.cratos.domain.view.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2024/1/2 17:53
 * @Version 1.0
 */
public class TagVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    public static class Tag extends BaseVO implements Serializable {

        @Serial
        private static final long serialVersionUID = -4367548299780720811L;

        private Integer id;

        private Integer tagType;

        private String tagKey;

        private String tagValue;

        /**
         * 颜色
         */
        private String color;

        private Integer promptColor;

        /**
         * 顺序
         */
        private Integer seq;


        private Boolean isActive;

        private String comment;

    }

}