package com.baiyi.cratos.domain.param.http.business;

import com.baiyi.cratos.domain.annotation.ApiModelPropertyPro;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessProperty;
import com.baiyi.cratos.domain.param.IToTarget;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/3/22 10:38
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class BusinessPropertyParam {

    @Data
    @Schema
    public static class SaveBusinessProperty implements IToTarget<BusinessProperty> {
        private Integer id;
        @NotBlank(message = "BusinessType must be specified.")
        @Schema(description = "Obtain from the enumeration class 'BusinessTypeEnum'")
        @ApiModelPropertyPro(value = BusinessTypeEnum.class)
        private String businessType;
        @NotNull(message = "BusinessId must be specified.")
        private Integer businessId;
        @NotBlank(message = "PropertyName must be specified.")
        private String propertyName;
        @NotBlank(message = "PropertyValue must be specified.")
        private String propertyValue;
    }

    @Data
    @Schema
    public static class AddBusinessProperty implements IToTarget<BusinessProperty> {
        @NotBlank(message = "BusinessType must be specified.")
        @Schema(description = "Obtain from the enumeration class 'BusinessTypeEnum'")
        @ApiModelPropertyPro(value = BusinessTypeEnum.class)
        private String businessType;
        @NotNull(message = "BusinessId must be specified.")
        private Integer businessId;
        @NotBlank(message = "PropertyName must be specified.")
        private String propertyName;
        @NotBlank(message = "PropertyValue must be specified.")
        private String propertyValue;
    }

    @Data
    @Schema
    public static class UpdateBusinessProperty implements IToTarget<BusinessProperty> {
        @NotNull(message = "ID must be specified.")
        private Integer id;
        @NotBlank(message = "PropertyValue must be specified.")
        private String propertyValue;
    }

}
