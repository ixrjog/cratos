package com.baiyi.cratos.domain.param.http.channel;

import com.baiyi.cratos.domain.generator.ChannelBusiness;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ChannelBusinessParam {

    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class AddChannelBusiness implements IToTarget<ChannelBusiness> {
        private Integer organizationId;
        private Integer channelId;
        private String businessName;
        private String type;
        private String businessDirection;
        private Boolean valid;
        private Integer seq;
        private String comment;
    }

    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class UpdateChannelBusiness implements IToTarget<ChannelBusiness> {
        private Integer id;
        private Integer organizationId;
        private Integer channelId;
        private String businessName;
        private String type;
        private String businessDirection;
        private Boolean valid;
        private Integer seq;
        private String comment;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class ChannelBusinessPageQuery extends PageParam {
        @Schema(description = "查询名称")
        private String queryName;
        private Integer channelId;
        private Integer organizationId;
    }

}
