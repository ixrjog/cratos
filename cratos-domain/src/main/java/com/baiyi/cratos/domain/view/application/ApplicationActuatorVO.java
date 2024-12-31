package com.baiyi.cratos.domain.view.application;

import com.baiyi.cratos.domain.view.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

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
    public static class ApplicationActuator extends BaseVO implements Serializable {
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
        private Boolean actuatorStandard;
        private Boolean lifecycleStandard;
        private Boolean standard;
        private String livenessProbe;
        private String readinessProbe;
        private String startupProbe;
        private String lifecycle;
        private String comment;

        private Container container;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Container implements Serializable {
        @Serial
        private static final long serialVersionUID = 5344460324717646868L;
        private Probe livenessProbe;
        private Probe readinessProbe;
        private Probe startupProbe;
        private Lifecycle lifecycle;

        private Boolean actuatorStandard;
        private Boolean lifecycleStandard;
        private Boolean standard;

        public void verify() {
            this.actuatorStandard = livenessProbe.getStandard() && this.readinessProbe.getStandard() && this.startupProbe.getStandard();
            this.lifecycleStandard = this.lifecycle.getStandard();
            this.standard = this.actuatorStandard && this.lifecycleStandard;
        }

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Probe implements Serializable {
        @Serial
        private static final long serialVersionUID = 5344460324717646868L;
        private String path;
        private Integer port;
        private Boolean standard;

        private Probe baseline;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Lifecycle implements Serializable {
        @Serial
        private static final long serialVersionUID = -3235610014871294865L;
        private String preStopExecCommand;
        private Boolean standard;

        private Lifecycle baseline;
    }

}
