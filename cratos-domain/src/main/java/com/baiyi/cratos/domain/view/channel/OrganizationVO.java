package com.baiyi.cratos.domain.view.channel;

import com.baiyi.cratos.domain.view.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/20 14:25
 * &#064;Version 1.0
 */
public class OrganizationVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    public static class Organization extends BaseVO implements Serializable {
        @Serial
        private static final long serialVersionUID = 6503307792076240814L;
        private Integer id;
        private String name;
        private String code;
        private String type;
        private Boolean valid;
        private String comment;
    }

}
