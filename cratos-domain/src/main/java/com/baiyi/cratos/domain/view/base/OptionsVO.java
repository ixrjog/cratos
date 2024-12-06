package com.baiyi.cratos.domain.view.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/1/12 09:36
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class OptionsVO {

    public static final OptionsVO.Options NO_OPTIONS_AVAILABLE = OptionsVO.Options.builder()
            .build();

    public static OptionsVO.Options toOptions(List<String> strings) {
        List<OptionsVO.Option> optionList = strings.stream()
                .map(e -> OptionsVO.Option.builder()
                        .label(e)
                        .value(e)
                        .build())
                .toList();
        return OptionsVO.Options.builder()
                .options(optionList)
                .build();
    }

    @Data
    @Schema
    @Builder
    public static class Options {
        private String message;
        @Builder.Default
        private List<Option> options = Collections.emptyList();
    }

    @Data
    @Schema
    @Builder
    public static class Option {
        private String label;
        private Object value;
        @Builder.Default
        private Boolean disabled = false;
        private Object comment;
    }

}
