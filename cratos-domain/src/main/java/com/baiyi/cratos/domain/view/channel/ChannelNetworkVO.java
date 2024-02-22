package com.baiyi.cratos.domain.view.channel;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.doc.BusinessDocVO;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/2/21 11:24
 * @Version 1.0
 */
public class ChannelNetworkVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.CHANNEL_NETWORK)
    public static class ChannelNetwork extends BaseVO implements BaseBusiness.IBusinessAnnotate,
            BusinessTagVO.IBusinessTags, BusinessDocVO.IBusinessDocs, Serializable {

        @Serial
        private static final long serialVersionUID = -4265038663652607939L;

        private Integer id;

        private String name;

        private String channelKey;

        private Boolean valid;

        private String channelStatus;

        private String availableStatus;

        private String comment;

        @Override
        public Integer getBusinessId() {
            return id;
        }

        @Schema(description = "Business Tags")
        List<BusinessTagVO.BusinessTag> businessTags;

        @Schema(description = "Business Docs")
        List<BusinessDocVO.BusinessDoc> businessDocs;

    }

}
