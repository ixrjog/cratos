package com.baiyi.cratos.domain.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/4/1 09:46
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class CachedVO {

    public interface ICached {
        void setCached(Cached cached);
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Cached implements Serializable {
        @Serial
        private static final long serialVersionUID = 323180444117750385L;
        @Builder.Default
        @Schema(description = "Create time")
        @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime = new Date();
        @Schema(description = "Cache duration(s)")
        @Builder.Default
        private Long duration = 600L;
    }

}
