package com.baiyi.cratos.domain.param.tag;

import com.baiyi.cratos.domain.BaseBusiness;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2024/1/5 17:54
 * @Version 1.0
 */
public class BusinessTagParam {

    @Data
    @Schema
    public static class GetByBusiness implements BaseBusiness.IBusiness {

        @NotNull
        private String businessType;

        @NotNull
        private Integer businessId;

    }

}
