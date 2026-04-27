package com.baiyi.cratos.service.channel;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.ChannelNode;
import com.baiyi.cratos.domain.param.http.channel.ChannelNodeParam;
import com.baiyi.cratos.mapper.ChannelNodeMapper;
import com.baiyi.cratos.service.base.BaseValidService;

import java.util.List;

public interface ChannelNodeService extends BaseValidService<ChannelNode, ChannelNodeMapper> {

    DataTable<ChannelNode> queryChannelNodePage(ChannelNodeParam.ChannelNodePageQuery pageQuery);

    List<ChannelNode> queryByChannelId(int channelId);

}
