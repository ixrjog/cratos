package com.baiyi.cratos.domain.param.tag;

import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * @Author baiyi
 * @Date 2024/1/2 17:37
 * @Version 1.0
 */
public class TagParam {

    @Data
    @Schema
    public static class Tag  {

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


        private Date createTime;


        private Date updateTime;
    }

    @Data
    @Schema
    public static class AddTag  {

        private Integer tagType;

        private String tagKey;

        private String tagValue;

        private String color;

        private Integer promptColor;

        private Integer seq;

        private Boolean isActive;

        private String comment;

    }

    @Data
    @Schema
    public static class UpdateTag  {

        private Integer id;

        private Integer tagType;

        private String tagKey;

        private String tagValue;

        private String color;

        private Integer promptColor;

        private Integer seq;

        private Boolean isActive;

        private String comment;

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @Schema
    public static class TagPageQuery extends PageParam {

        @Schema(description = "标签Key")
        private String tagKey;

        @Schema(description = "业务类型", example = "0")
        private Integer businessType;

    }

}