package com.baiyi.cratos.domain.param.business;

import com.baiyi.cratos.domain.BaseBusiness;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2024/1/9 09:56
 * @Version 1.0
 */
public class BusinessParam {

    @Data
    @Schema
    public static class GetByBusiness implements BaseBusiness.HasBusiness {

        @NotNull
        private String businessType;

        @NotNull
        private Integer businessId;

    }

    @Data
    @Schema
    public static class QueryByBusinessType implements BaseBusiness.HasBusinessType {

        @NotNull
        private String businessType;

    }

}
