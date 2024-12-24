package com.baiyi.cratos.domain.view.application;

import com.baiyi.cratos.domain.view.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/24 10:27
 * &#064;Version 1.0
 */
public class ApplicationActuatorVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    public static class ApplicationActuator extends BaseVO implements  Serializable {
        @Serial
        private static final long serialVersionUID = 6876457686001577045L;
        private Integer id;
        private String applicationName;
        private String instanceName;
        private String name;
        private String displayName;
        private String resourceType;
        private Integer businessId;
        private String businessType;
        private String namespace;
        private String framework;
        private Boolean standard;
        private String livenessProbe;
        private String readinessProbe;
        private String startupProbe;
        private String lifecycle;
        private String comment;
    }

}
