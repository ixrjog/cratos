package com.baiyi.cratos.domain.param.http.business;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.annotation.ApiModelPropertyPro;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/1/9 09:56
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class BusinessParam {

    @Data
    @Schema
    public static class GetByBusiness implements BaseBusiness.HasBusiness {
        @NotNull
        @Schema(description = "Obtain from the enumeration class 'BusinessTypeEnum'")
        @ApiModelPropertyPro(value = BusinessTypeEnum.class)
        private String businessType;
        @NotNull
        private Integer businessId;
    }

    @Data
    @Schema
    public static class QueryByBusinessType implements BaseBusiness.HasBusinessType {
        @NotNull
        @Schema(description = "Obtain from the enumeration class 'BusinessTypeEnum'")
        @ApiModelPropertyPro(value = BusinessTypeEnum.class)
        private String businessType;
    }

}
