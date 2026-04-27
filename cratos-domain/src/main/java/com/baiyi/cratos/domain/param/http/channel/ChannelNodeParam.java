package com.baiyi.cratos.domain.param.http.channel;

import com.baiyi.cratos.domain.generator.ChannelNode;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ChannelNodeParam {

    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class AddChannelNode implements IToTarget<ChannelNode> {
        private Integer channelId;
        private String name;
        private String nodeType;
        private String sourceEndpoint;
        private String monitorUrl;
        private Boolean linkedChannel;
        private Boolean valid;
        private String comment;
        private String nodeInfo;
    }

    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class UpdateChannelNode implements IToTarget<ChannelNode> {
        private Integer id;
        private Integer channelId;
        private String name;
        private String nodeType;
        private String sourceEndpoint;
        private String monitorUrl;
        private Boolean linkedChannel;
        private Boolean valid;
        private String comment;
        private String nodeInfo;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class ChannelNodePageQuery extends PageParam {
        @Schema(description = "查询名称")
        private String queryName;
        private Integer channelId;
    }
}
