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
 * &#064;Date  2025/5/19 10:45
 * &#064;Version 1.0
 */
public class AliyunModel {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AliyunAccount implements Serializable {
        @Serial
        private static final long serialVersionUID = -8707920768026453756L;
        private EdsInstanceVO.EdsInstance edsInstance;
        private String username;
        // Aliyun RAM Username
        private String account;
        private String loginLink;
    }

}
