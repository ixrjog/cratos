package com.baiyi.cratos.domain.param.http.eds;

import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/2/5 17:49
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class EdsConfigParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class EdsConfigPageQuery extends PageParam {
        private String queryName;
        @Schema(description = "EDS Type")
        private String edsType;
        private Boolean valid;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class AddEdsConfig implements IToTarget<EdsConfig> {
        private Integer id;
        private String name;
        private String edsType;
        private String version;
        private Boolean valid;
        private Integer credentialId;
        private Integer instanceId;
        private String url;
        private String configContent;
        private String comment;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class UpdateEdsConfig implements IToTarget<EdsConfig> {
        private Integer id;
        private String name;
        private String edsType;
        private String version;
        private Boolean valid;
        private Integer credentialId;
        private Integer instanceId;
        private String url;
        private String configContent;
        private String comment;
    }

}
