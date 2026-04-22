package com.baiyi.cratos.domain.param.http.channel;

import com.baiyi.cratos.domain.generator.ChannelBusinessLine;
import com.baiyi.cratos.domain.param.IToTarget;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ChannelBusinessLineParam {

    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class AddChannelBusinessLine implements IToTarget<ChannelBusinessLine> {
        private Integer channelBusinessId;
        private Integer channelLineId;
        private Boolean valid;
        private String comment;
    }
}
