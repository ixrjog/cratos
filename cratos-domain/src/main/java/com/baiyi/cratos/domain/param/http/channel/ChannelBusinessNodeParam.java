package com.baiyi.cratos.domain.param.http.channel;

import com.baiyi.cratos.domain.generator.ChannelBusinessNode;
import com.baiyi.cratos.domain.param.IToTarget;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ChannelBusinessNodeParam {

    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class AddChannelBusinessNode implements IToTarget<ChannelBusinessNode> {
        private Integer channelBusinessId;
        private Integer channelNodeId;
        private Boolean valid;
        private String comment;
    }
}
