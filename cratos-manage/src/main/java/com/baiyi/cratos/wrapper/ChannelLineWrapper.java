package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Channel;
import com.baiyi.cratos.domain.generator.ChannelLine;
import com.baiyi.cratos.domain.util.BeanCopierUtils;
import com.baiyi.cratos.domain.view.channel.ChannelLineVO;
import com.baiyi.cratos.service.channel.ChannelBusinessLineService;
import com.baiyi.cratos.service.channel.ChannelLineService;
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
public class ChannelLineWrapper extends BaseDataTableConverter<ChannelLineVO.Line, ChannelLine> implements BaseBusinessDecorator<ChannelLineVO.HasChannelBusinessLines, ChannelLineVO.Line> {

    private final ChannelService channelService;
    private final ChannelLineService channelLineService;
    private final ChannelBusinessLineService channelBusinessLineService;

    @Override
    public void wrap(ChannelLineVO.Line vo) {
        try {
            Channel channel = channelService.getById(vo.getChannelId());
            if (channel != null) {
                vo.setChannelName(channel.getName());
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void decorateBusiness(ChannelLineVO.HasChannelBusinessLines hasBusiness) {
        try {
            hasBusiness.setLines(channelBusinessLineService.queryByChannelBusinessId(hasBusiness.getChannelBusinessId())
                                         .stream()
                                         .map(e -> {
                                             ChannelLine line = channelLineService.getById(e.getChannelLineId());
                                             return BeanCopierUtils.copyProperties(line, ChannelLineVO.Line.class);
                                         })
                                         .toList());
        } catch (Exception ignored) {
        }
    }

}
