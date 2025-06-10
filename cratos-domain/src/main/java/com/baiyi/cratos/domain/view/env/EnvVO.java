package com.baiyi.cratos.domain.view.env;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

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
    @BusinessType(type = BusinessTypeEnum.ENV)
    public static class Env extends BaseVO implements BaseBusiness.IBusinessAnnotate, BusinessTagVO.HasBusinessTags, Serializable {
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

        @Override
        public Integer getBusinessId() {
            return id;
        }

        @Schema(description = "Business Tags")
        private List<BusinessTagVO.BusinessTag> businessTags;
    }

}
