package com.baiyi.cratos.domain.model;

import com.baiyi.cratos.domain.view.user.UserVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/3 09:58
 * &#064;Version 1.0
 */
public class RiskChangeModel {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class RiskChangeApplication implements Serializable {
        @Serial
        private static final long serialVersionUID = 5215226390679158598L;
        private UserVO.User applicant;
        private String title;
        private String content;
    }

}
