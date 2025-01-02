package com.baiyi.cratos.domain.view.application;

import com.baiyi.cratos.domain.view.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/30 17:09
 * &#064;Version 1.0
 */
public class ApplicationResourceBaselineVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    public static class ResourceBaseline extends BaseVO implements Serializable {
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

        private Map<String, EnvVar> env;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Probe implements Serializable {
        @Serial
        private static final long serialVersionUID = 5344460324717646868L;
        public static final Probe EMPTY = Probe.builder()
                .build();
        private String path;
        private Integer port;
        private Boolean standard;
        private String content;
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
        public static final Lifecycle EMPTY = Lifecycle.builder()
                .build();
        private String preStopExecCommand;
        private Boolean standard;
        private String content;
        private Lifecycle baseline;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EnvVar implements Serializable {
        @Serial
        private static final long serialVersionUID = -5743384787968066582L;
        public static final EnvVar EMPTY = EnvVar.builder()
                .build();
        private String name;
        private String value;
        private EnvVarSource valueFrom;
        private Boolean standard;
        private String content;
        private EnvVar baseline;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EnvVarSource implements Serializable {
        @Serial
        private static final long serialVersionUID = 3557401523761193340L;
        private ConfigMapKeySelector configMapKeyRef;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ConfigMapKeySelector implements Serializable  {
        @Serial
        private static final long serialVersionUID = 5380033101181848519L;
        private String key;
        private String name;
        private Boolean optional;
    }

}
