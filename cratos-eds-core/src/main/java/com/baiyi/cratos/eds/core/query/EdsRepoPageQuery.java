package com.baiyi.cratos.eds.core.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2024/2/26 13:20
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema
public class EdsRepoPageQuery {

    @Builder.Default
    private Long page = 1L;

    @Builder.Default
    private Long length = 50L;

}
