package com.baiyi.cratos.domain.view.channel;

import com.baiyi.cratos.domain.view.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class ChannelLineVO {

    public interface HasChannelBusinessLines {
        Integer getChannelBusinessId();

        void setLines(List<Line> lines);
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    public static class Line extends BaseVO implements Serializable {
        @Serial
        private static final long serialVersionUID = -1353416654145553636L;
        private Integer id;
        private Integer channelId;
        private String name;
        private String lineType;
        private String sourceEndpoint;
        private String monitorUrl;
        private Boolean linkedChannel;
        private Boolean valid;
        private String comment;
        private String channelName;
    }

}
