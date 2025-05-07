package com.baiyi.cratos.domain.model;

import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/6 11:39
 * &#064;Version 1.0
 */
public class AliyunDataWorksModel {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AliyunAccount implements Serializable {
        @Serial
        private static final long serialVersionUID = -5846538132637578197L;
        private EdsInstanceVO.EdsInstance edsInstance;
        private String username;
        // Aliyun DataWorks RAM Username
        private String account;
    }

}
