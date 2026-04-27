package com.baiyi.cratos.facade.channel.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.ChannelNode;
import com.baiyi.cratos.domain.param.http.channel.ChannelBusinessNodeParam;
import com.baiyi.cratos.domain.param.http.channel.ChannelNodeParam;
import com.baiyi.cratos.domain.util.BeanCopierUtils;
import com.baiyi.cratos.domain.view.channel.ChannelNodeVO;
import com.baiyi.cratos.facade.channel.ChannelNodeFacade;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.service.channel.ChannelBusinessNodeService;
import com.baiyi.cratos.service.channel.ChannelNodeService;
import com.baiyi.cratos.wrapper.ChannelNodeWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChannelNodeFacadeImpl implements ChannelNodeFacade {
    private final ChannelNodeService channelNodeService;
    private final ChannelBusinessNodeService channelBusinessNodeService;
    private final ChannelNodeWrapper channelNodeWrapper;

    @Override
    public DataTable<ChannelNodeVO.Node> queryChannelNodePage(ChannelNodeParam.ChannelNodePageQuery pageQuery) {
        DataTable<ChannelNode> table = channelNodeService.queryChannelNodePage(pageQuery);
        return channelNodeWrapper.wrapToTarget(table);
    }

    @Override
    public void addChannelNode(ChannelNodeParam.AddChannelNode addChannelNode) {
        channelNodeService.add(addChannelNode.toTarget());
    }
 
    @Override
    public void updateChannelNode(ChannelNodeParam.UpdateChannelNode updateChannelNode) {
        channelNodeService.updateByPrimaryKey(updateChannelNode.toTarget());
    }

    @Override
    public void deleteById(int id) {
        channelNodeService.deleteById(id);
    }

    @Override
    public void addChannelBusinessNode(ChannelBusinessNodeParam.AddChannelBusinessNode param) {
        channelBusinessNodeService.add(param.toTarget());
    }

    @Override
    public void deleteChannelBusinessNodeById(int id) {
        channelBusinessNodeService.deleteById(id);
    }

    @Override
    public List<ChannelNodeVO.Node> queryChannelBusinessNodes(int channelBusinessId) {
        return channelBusinessNodeService.queryByChannelBusinessId(channelBusinessId)
                .stream()
                .map(e -> BeanCopierUtils.copyProperties(
                        channelNodeService.getById(e.getChannelNodeId()), ChannelNodeVO.Node.class))
                .toList();
    }

    @Override
    public BaseValidService<?, ?> getValidService() {
        return channelNodeService;
    }
}
