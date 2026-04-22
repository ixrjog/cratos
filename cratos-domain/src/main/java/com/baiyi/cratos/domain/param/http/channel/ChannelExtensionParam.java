package com.baiyi.cratos.domain.param.http.channel;

import com.baiyi.cratos.domain.generator.ChannelExtension;
import com.baiyi.cratos.domain.param.IToTarget;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ChannelExtensionParam {

    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class AddChannelExtension implements IToTarget<ChannelExtension> {
        private Integer channelId;
        private String businessType;
        private Integer businessId;
        private String role;
        private String name;
        private Boolean valid;
        private String comment;
    }

}
