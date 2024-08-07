package com.baiyi.cratos.domain.view.tag;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.view.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/1/2 17:53
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class TagVO {

    public interface HasTag extends BaseBusiness.HasBusiness {
        Integer getTagId();
        void setTag(Tag businessTags);
        Tag getTag();
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    public static class Tag extends BaseVO implements Serializable {
        @Serial
        private static final long serialVersionUID = -4367548299780720811L;
        private Integer id;
        private String tagType;
        private String tagKey;
        private String tagValue;
        /**
         * 颜色
         */
        private String color;
        private String promptColor;
        /**
         * 顺序
         */
        private Integer seq;
        private Boolean valid;
        private String comment;
    }

}