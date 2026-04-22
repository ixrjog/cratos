package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Channel;
import com.baiyi.cratos.domain.generator.ChannelLine;
import com.baiyi.cratos.domain.util.BeanCopierUtils;
import com.baiyi.cratos.domain.view.channel.ChannelLineVO;
import com.baiyi.cratos.service.channel.ChannelLineService;
import com.baiyi.cratos.service.channel.ChannelService;
import com.baiyi.cratos.wrapper.base.BaseBusinessDecorator;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.CHANNEL_LINE)
public class ChannelLineWrapper extends BaseDataTableConverter<ChannelLineVO.Line, ChannelLine> implements BaseBusinessDecorator<ChannelLineVO.HasChannelLines, ChannelLineVO.Line> {

    private final ChannelService channelService;
    private final ChannelLineService channelLineService;

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
    public void decorateBusiness(ChannelLineVO.HasChannelLines hasBusiness) {
        try {
            List<ChannelLine> lines = channelLineService.queryByChannelId(hasBusiness.getChannelId());
            hasBusiness.setLines(BeanCopierUtils.copyListProperties(lines, ChannelLineVO.Line.class));
        } catch (Exception ignored) {
        }
    }

}
