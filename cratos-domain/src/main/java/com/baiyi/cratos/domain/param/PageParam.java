package com.baiyi.cratos.domain.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Schema
public class PageParam {

    @Schema(description = "分页页码")
    @Builder.Default
    private Integer page = 1;

    @Max(value = 1024, message = "分页查询最大限制1024条记录")
    @Schema(description = "分页页长", example = "10")
    @Builder.Default
    private Integer length = 10;

}