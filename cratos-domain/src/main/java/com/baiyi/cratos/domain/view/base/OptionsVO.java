package com.baiyi.cratos.domain.view.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;
import java.util.Objects;

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
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Options {
        private String message;
        @Builder.Default
        private List<Option> options = List.of();
    }

    @Data
    @Schema
    @Builder
    public static class Option implements Comparable<Option> {
        private String label;
        private Object value;
        @Builder.Default
        private Boolean disabled = false;
        private Integer seq;
        private Object comment;

        @Override
        public int compareTo(@NonNull Option o) {
            if (Objects.isNull(this.seq)) {
                return this.value.toString()
                        .compareTo(o.value.toString());
            }
            return this.seq.compareTo(o.getSeq());
        }
    }

}
