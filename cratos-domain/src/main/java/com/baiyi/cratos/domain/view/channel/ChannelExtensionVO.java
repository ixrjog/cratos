package com.baiyi.cratos.domain.view.channel;

import com.baiyi.cratos.domain.view.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/28 10:39
 * &#064;Version 1.0
 */
public class ChannelExtensionVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    public static class Extension extends BaseVO implements Serializable {
        @Serial
        private static final long serialVersionUID = 3596929846696931018L;
        private Integer id;
        private Integer channelId;
        private String businessType;
        private Integer businessId;
        private String role;
        private String name;
        private Boolean valid;
        private String comment;
        private Object extObj;
    }

}
