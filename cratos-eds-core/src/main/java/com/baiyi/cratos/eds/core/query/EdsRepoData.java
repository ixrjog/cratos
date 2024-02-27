package com.baiyi.cratos.eds.core.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/2/26 13:22
 * @Version 1.0
 */
@Schema
@Data
@Builder
@AllArgsConstructor
public class EdsRepoData<T> {

    public final static EdsRepoData<?> EMPTY =  EdsRepoData.builder()
            .build();

    @Schema(description = "分页数据")
    private List<T> data;

    @Schema(description = "当前页码")
    private long nowPage;

    @Schema(description = "总记录数")
    private long totalNum;

    public EdsRepoData(List<T> data, long totalNum) {
        this.data = data;
        this.totalNum = totalNum;
    }

    public EdsRepoData() {
        this.data = Collections.emptyList();
        this.totalNum = 0;
    }

}
