package com.baiyi.cratos.domain.param.channel;

import com.baiyi.cratos.domain.generator.ChannelNetwork;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import com.baiyi.cratos.domain.param.tag.BusinessTagParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/2/21 11:27
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class ChannelNetworkParam {

    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class AddChannelNetwork implements IToTarget<ChannelNetwork> {
        private String name;
        private String channelKey;
        private Boolean valid;
        private String channelStatus;
        private String availableStatus;
        private String comment;
    }

    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class UpdateChannelNetwork implements IToTarget<ChannelNetwork> {
        private Integer id;
        private String name;
        private String channelKey;
        private Boolean valid;
        private String channelStatus;
        private String availableStatus;
        private String comment;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class ChannelNetworkPageQuery extends PageParam implements BusinessTagParam.HasQueryByTag {
        @Schema(description = "查询名称")
        private String queryName;
        private BusinessTagParam.QueryByTag queryByTag;
        private List<Integer> idList;
        public ChannelNetworkPageQueryParam toParam() {
            return ChannelNetworkPageQueryParam.builder()
                    .page(getPage())
                    .length(getLength())
                    .queryName(queryName)
                    .idList(idList)
                    .build();
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class ChannelNetworkPageQueryParam extends PageParam {
        private String queryName;
        private List<Integer> idList;
    }

}
