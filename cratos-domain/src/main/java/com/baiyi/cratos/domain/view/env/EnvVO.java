package com.baiyi.cratos.domain.view.env;

import com.baiyi.cratos.domain.view.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/3/19 14:19
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class EnvVO {

    public interface HasEnv {
        String getEnvName();
        void setEnv(Env env);
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    public static class Env extends BaseVO implements Serializable {
        @Serial
        private static final long serialVersionUID = 4975003120740382381L;
        private Integer id;
        private String envName;
        private String color;
        private String promptColor;
        private Integer lifecycle;
        private Integer seq;
        private Boolean valid;
        private String comment;
    }

}
