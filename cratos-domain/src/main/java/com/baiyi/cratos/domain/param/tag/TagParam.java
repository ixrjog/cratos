package com.baiyi.cratos.domain.param.tag;

import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;


/**
 * @Author baiyi
 * @Date 2024/1/2 17:37
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class TagParam {

    @Data
    @Schema
    public static class AddTag implements IToTarget<Tag> {

        private String tagType;

        private String tagKey;

        private String tagValue;

        private String color;

        private String promptColor;

        private Integer seq;

        private Boolean valid;

        private String comment;

    }

    @Data
    @Schema
    public static class UpdateTag implements IToTarget<Tag> {

        private Integer id;

        private String tagType;

        private String tagKey;

        private String tagValue;

        private String color;

        private String promptColor;

        private Integer seq;

        private Boolean valid;

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