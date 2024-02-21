package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.ChannelNetwork;
import com.baiyi.cratos.domain.param.channel.ChannelNetworkParam;
import com.baiyi.cratos.domain.view.channel.ChannelNetworkVO;
import com.baiyi.cratos.facade.ChannelNetworkFacade;
import com.baiyi.cratos.service.ChannelNetworkService;
import com.baiyi.cratos.wrapper.ChannelNetworkWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/2/21 11:17
 * @Version 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class ChannelNetworkFacadeImpl implements ChannelNetworkFacade {

    private final ChannelNetworkService channelNetworkService;

    private final ChannelNetworkWrapper channelNetworkWrapper;

    @Override
    public DataTable<ChannelNetworkVO.ChannelNetwork> queryChannelNetworkPage(ChannelNetworkParam.ChannelNetworkPageQuery pageQuery) {
        DataTable<ChannelNetwork> table = channelNetworkService.queryChannelNetworkPage(pageQuery);
        return channelNetworkWrapper.wrapToTarget(table);
    }

    @Override
    public void setChannelNetworkValidById(int id) {
        channelNetworkService.updateValidById(id);
    }

    @Override
    public void addChannelNetwork(ChannelNetworkParam.AddChannelNetwork addChannelNetwork) {
        channelNetworkService.add(addChannelNetwork.toTarget());
    }

    @Override
    public void updateChannelNetwork(ChannelNetworkParam.UpdateChannelNetwork updateChannelNetwork) {
        channelNetworkService.updateByPrimaryKey(updateChannelNetwork.toTarget());
    }

}
