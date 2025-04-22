package com.baiyi.cratos.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;


@SuppressWarnings("rawtypes")
@Schema
@Data
public class DataTable<T> {
    public final static DataTable NO_DATA = new DataTable<>();
    @Schema(description = "分页数据")
    private List<T> data;
    @Schema(description = "当前页码")
    private int nowPage;
    @Schema(description = "总记录数")
    private long totalNum;

    public DataTable(List<T> data, long totalNum) {
        this.data = CollectionUtils.isEmpty(data) ? List.of() : data;
        this.totalNum = totalNum;
    }

    public DataTable() {
        this.data = Collections.emptyList();
        this.totalNum = 0;
    }

}