package com.baiyi.cratos.domain.param.http.channel;

import com.baiyi.cratos.domain.generator.ChannelLine;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ChannelLineParam {

    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class AddChannelLine implements IToTarget<ChannelLine> {
        private Integer channelId;
        private String name;
        private String lineType;
        private String sourceEndpoint;
        private String monitorUrl;
        private Boolean linkedChannel;
        private Boolean valid;
        private String comment;
    }

    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class UpdateChannelLine implements IToTarget<ChannelLine> {
        private Integer id;
        private Integer channelId;
        private String name;
        private String lineType;
        private String sourceEndpoint;
        private String monitorUrl;
        private Boolean linkedChannel;
        private Boolean valid;
        private String comment;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class ChannelLinePageQuery extends PageParam {
        @Schema(description = "查询名称")
        private String queryName;
        private Integer channelId;
    }
}
