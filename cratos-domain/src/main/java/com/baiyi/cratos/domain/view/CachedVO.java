package com.baiyi.cratos.domain.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2024/4/1 09:46
 * @Version 1.0
 */
public class CachedVO {

    public interface ICached {

        void setCached(Cached cached);
    }

    @Data
    public static class Cached {

        @Schema(description = "Create time")
        @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;

        @Schema(description = "Cache duration(s)")
        private Long duration;

    }

}
