package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.annotation.PageQueryByTag;
import com.baiyi.cratos.common.enums.ChannelAvailableStatusEnum;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.ChannelNetwork;
import com.baiyi.cratos.domain.param.http.channel.ChannelNetworkParam;
import com.baiyi.cratos.domain.view.channel.ChannelNetworkVO;
import com.baiyi.cratos.facade.ChannelNetworkFacade;
import com.baiyi.cratos.service.ChannelNetworkService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.wrapper.ChannelNetworkWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/2/21 11:17
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ChannelNetworkFacadeImpl implements ChannelNetworkFacade {

    private final ChannelNetworkService channelNetworkService;
    private final ChannelNetworkWrapper channelNetworkWrapper;

    @Override
    @PageQueryByTag(typeOf = BusinessTypeEnum.CHANNEL_NETWORK)
    public DataTable<ChannelNetworkVO.ChannelNetwork> queryChannelNetworkPage(
            ChannelNetworkParam.ChannelNetworkPageQuery pageQuery) {
        DataTable<ChannelNetwork> table = channelNetworkService.queryChannelNetworkPage(pageQuery.toParam());
        return channelNetworkWrapper.wrapToTarget(table);
    }

    @Override
    public void addChannelNetwork(ChannelNetworkParam.AddChannelNetwork addChannelNetwork) {
        ChannelAvailableStatusEnum.verifyValueOf(addChannelNetwork.getAvailableStatus());
        channelNetworkService.add(addChannelNetwork.toTarget());
    }

    @Override
    public void updateChannelNetwork(ChannelNetworkParam.UpdateChannelNetwork updateChannelNetwork) {
        ChannelAvailableStatusEnum.verifyValueOf(updateChannelNetwork.getAvailableStatus());
        channelNetworkService.updateByPrimaryKey(updateChannelNetwork.toTarget());
    }

    @Override
    public void deleteById(int id) {
        channelNetworkService.deleteById(id);
    }

    @Override
    public BaseValidService<?, ?> getValidService() {
        return channelNetworkService;
    }
}
