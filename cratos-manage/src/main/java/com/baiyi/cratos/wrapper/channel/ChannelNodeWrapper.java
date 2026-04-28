package com.baiyi.cratos.wrapper.channel;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Channel;
import com.baiyi.cratos.domain.generator.ChannelNode;
import com.baiyi.cratos.domain.util.BeanCopierUtils;
import com.baiyi.cratos.domain.view.channel.ChannelNodeVO;
import com.baiyi.cratos.service.channel.ChannelBusinessNodeService;
import com.baiyi.cratos.service.channel.ChannelNodeService;
import com.baiyi.cratos.service.channel.ChannelService;
import com.baiyi.cratos.wrapper.base.BaseBusinessDecorator;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.CHANNEL_LINE)
public class ChannelNodeWrapper extends BaseDataTableConverter<ChannelNodeVO.Node, ChannelNode> implements BaseBusinessDecorator<ChannelNodeVO.HasChannelBusinessNodes, ChannelNodeVO.Node> {

    private final ChannelService channelService;
    private final ChannelNodeService channelNodeService;
    private final ChannelBusinessNodeService channelBusinessNodeService;

    @Override
    public void wrap(ChannelNodeVO.Node vo) {
        try {
            Channel channel = channelService.getById(vo.getChannelId());
            if (channel != null) {
                vo.setChannelName(channel.getName());
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void decorateBusiness(ChannelNodeVO.HasChannelBusinessNodes hasBusiness) {
        try {
            hasBusiness.setNodes(channelBusinessNodeService.queryByChannelBusinessId(hasBusiness.getChannelBusinessId())
                                         .stream()
                                         .map(e -> {
                                             ChannelNode node = channelNodeService.getById(e.getChannelNodeId());
                                             return BeanCopierUtils.copyProperties(node, ChannelNodeVO.Node.class);
                                         })
                                         .toList());
        } catch (Exception ignored) {
        }
    }

}
