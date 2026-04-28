package com.baiyi.cratos.domain.param.http.channel;

import com.baiyi.cratos.domain.generator.Channel;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ChannelParam {

    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class AddChannel implements IToTarget<Channel> {
        private String name;
        private String monitorUrl;
        private String priority;
        private String country;
        private String networkInfo;
        private String availableStatus;
        private String constructionPhase;
        private Boolean valid;
        private String comment;
    }

    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class UpdateChannel implements IToTarget<Channel> {
        private Integer id;
        private String name;
        private String monitorUrl;
        private String priority;
        private String country;
        private String networkInfo;
        private String availableStatus;
        private String constructionPhase;
        private Boolean valid;
        private String comment;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class ChannelPageQuery extends PageParam {
        @Schema(description = "查询名称")
        private String queryName;
        @Schema(description = "国家码")
        private String country;
    }

    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class CallAlert {
        private Integer channelId;
        private List<String> usernames;
    }

}
