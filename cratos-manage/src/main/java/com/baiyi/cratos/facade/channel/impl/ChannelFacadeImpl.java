package com.baiyi.cratos.facade.channel.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Channel;
import com.baiyi.cratos.domain.generator.ChannelExtension;
import com.baiyi.cratos.domain.param.http.channel.ChannelExtensionParam;
import com.baiyi.cratos.domain.param.http.channel.ChannelParam;
import com.baiyi.cratos.domain.view.channel.ChannelVO;
import com.baiyi.cratos.facade.channel.ChannelFacade;
import com.baiyi.cratos.service.channel.ChannelService;
import com.baiyi.cratos.service.channel.ChannelExtensionService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.wrapper.ChannelWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChannelFacadeImpl implements ChannelFacade {

    private final ChannelService channelService;
    private final ChannelExtensionService channelExtensionService;
    private final ChannelWrapper channelWrapper;

    @Override
    public DataTable<ChannelVO.Channel> queryChannelPage(ChannelParam.ChannelPageQuery pageQuery) {
        DataTable<Channel> table = channelService.queryChannelPage(pageQuery);
        return channelWrapper.wrapToTarget(table);
    }

    @Override
    public void addChannel(ChannelParam.AddChannel addChannel) {
        channelService.add(addChannel.toTarget());
    }

    @Override
    public void updateChannel(ChannelParam.UpdateChannel updateChannel) {
        channelService.updateByPrimaryKey(updateChannel.toTarget());
    }

    @Override
    public void deleteById(int id) {
        channelService.deleteById(id);
    }

    @Override
    public List<ChannelExtension> queryChannelExtensions(int channelId) {
        return channelExtensionService.queryByChannelId(channelId);
    }

    @Override
    public void addChannelExtension(ChannelExtensionParam.AddChannelExtension addChannelExtension) {
        channelExtensionService.add(addChannelExtension.toTarget());
    }

    @Override
    public void deleteChannelExtensionById(int id) {
        channelExtensionService.deleteById(id);
    }

    @Override
    public BaseValidService<?, ?> getValidService() {
        return channelService;
    }

}
