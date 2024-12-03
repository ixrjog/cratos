package com.baiyi.cratos.eds.opscloud.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/2 11:11
 * &#064;Version 1.0
 */
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema
public class HasPageParam {

    @Schema(description = "分页页码")
    @Builder.Default
    private Integer page = 1;

    @Max(value = 1024, message = "分页查询最大限制1024条记录")
    @Schema(description = "分页页长", example = "10")
    private Integer length;
}
