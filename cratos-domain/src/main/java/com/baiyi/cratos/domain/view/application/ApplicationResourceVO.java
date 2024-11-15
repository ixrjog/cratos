package com.baiyi.cratos.domain.view.application;

import com.baiyi.cratos.domain.view.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/15 11:32
 * &#064;Version 1.0
 */
public class ApplicationResourceVO {

    public interface HasApplicationResources {
        String getApplicationName();

        void setResources(Map<String, List<Resource>> resources);
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    public static class Resource extends BaseVO implements Serializable {
        @Serial
        private static final long serialVersionUID = -6075619901692988480L;
        private Integer id;
        private String applicationName;
        private String name;
        private String resourceType;
        private String businessType;
        private Integer businessId;
        private String comment;
    }

}
