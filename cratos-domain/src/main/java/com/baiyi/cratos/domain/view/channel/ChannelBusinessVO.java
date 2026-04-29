package com.baiyi.cratos.domain.view.channel;

import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.account.AccountEntityVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class ChannelBusinessVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    public static class Business extends BaseVO implements ChannelVO.HasChannel, ChannelNodeVO.HasChannelBusinessNodes, OrganizationVO.HasOrganization, AccountEntityVO.HasAccountEntity, Serializable {
        @Serial
        private static final long serialVersionUID = 4148816104425592602L;
        private Integer id;
        private Integer organizationId;
        private Integer accountEntityId;
        private Integer channelId;
        private String businessName;
        private String type;
        private String businessDirection;
        private Boolean valid;
        private Integer seq;
        private String comment;

        private List<ChannelNodeVO.Node> nodes;
        private AccountEntityVO.AccountEntity accountEntity;

        public Integer getChannelBusinessId() {
            return this.id;
        }

        private OrganizationVO.Organization organization;
        private ChannelVO.Channel channel;
    }

}
