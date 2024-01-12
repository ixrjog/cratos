package com.baiyi.cratos.domain.view.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/12 09:36
 * @Version 1.0
 */
public class OptionsVO {

    @Data
    @Schema
    @Builder
    public static class Options {
        private List<Option> options;
    }

    @Data
    @Schema
    @Builder
    public static class Option {
        private String label;
        private Object value;
        private Object comment;
    }

}
