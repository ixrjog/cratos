package com.baiyi.cratos.domain.view.cratos;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.enums.InstanceHealthStatus;
import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/7 10:48
 * &#064;Version 1.0
 */
public class CratosInstanceVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.CRATOS_INSTANCE)
    public static class RegisteredInstance extends BaseVO implements  BaseBusiness.IBusinessAnnotate, BusinessTagVO.HasBusinessTags, Serializable {
        @Serial
        private static final long serialVersionUID = -7855855047753656754L;

        private Integer id;
        private String name;
        private String hostname;
        private String hostIp;
        private String status;
        private Boolean valid;
        private String version;
        private String comment;
        @JsonFormat(timezone = "UTC", pattern = Global.ISO8601)
        private Date startTime;
        private String commit;
        private String license;

        @Override
        public Integer getBusinessId() {
            return id;
        }

        @Schema(description = "Business Tags")
        private List<BusinessTagVO.BusinessTag> businessTags;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class Health implements Serializable {
        @Serial
        private static final long serialVersionUID = -3384527553459992674L;
        private String status;
        private boolean isHealth;

        public static Health of(InstanceHealthStatus status) {
            return Health.builder()
                    .status(status.name())
                    .isHealth(status.equals(InstanceHealthStatus.OK))
                    .build();
        }
    }

}
