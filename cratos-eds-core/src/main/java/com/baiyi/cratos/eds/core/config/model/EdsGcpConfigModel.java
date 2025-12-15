package com.baiyi.cratos.eds.core.config.model;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import com.baiyi.cratos.eds.core.exception.EdsConfigException;
import com.google.iam.admin.v1.ProjectName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/29 上午10:19
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class EdsGcpConfigModel {

    @Data
    @NoArgsConstructor
    public static class Gcp implements IEdsConfigModel {
        private Project project;
        private EdsInstance edsInstance;
        private Certificate certificate;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Project implements ToProjectName {
        private String id;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Certificate {
        // https://console.cloud.google.com/security/ccm/certificates/add?hl=zh-cn&orgonly=true&project=palmpay-nigeria&supportedpurview=organizationId
        private List<String> locations;
    }

    public interface ToProjectName {
        String getId();

        default String toProjectName() {
            if (!StringUtils.hasText(getId())) {
                throw new EdsConfigException("project id cannot be empty.");
            }
            return ProjectName.of(getId()).toString();
        }

    }

}