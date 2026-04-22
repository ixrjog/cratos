package com.baiyi.cratos.domain.view.channel;

import com.baiyi.cratos.domain.view.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ChannelVO {

    public interface HasChannel {
        Integer getChannelId();

        void setChannel(Channel channel);
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    public static class Channel extends BaseVO implements Serializable {

        @Serial
        private static final long serialVersionUID = 2799944282487832823L;
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
        private Map<String, List<Member>> members;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    public static class Member extends BaseVO implements Serializable {
        @Serial
        private static final long serialVersionUID = -5361945051520027443L;
        private Integer id;
        private Integer channelId;
        private String businessType;
        private Integer businessId;
        private String role;
        private String name;
        private Boolean valid;
        private String comment;
    }

}
