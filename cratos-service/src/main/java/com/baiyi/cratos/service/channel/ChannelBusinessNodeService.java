package com.baiyi.cratos.service.channel;

import com.baiyi.cratos.domain.generator.ChannelBusinessNode;
import com.baiyi.cratos.mapper.ChannelBusinessNodeMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;

import java.util.List;

public interface ChannelBusinessNodeService extends BaseUniqueKeyService<ChannelBusinessNode, ChannelBusinessNodeMapper>, BaseValidService<ChannelBusinessNode, ChannelBusinessNodeMapper> {

    List<ChannelBusinessNode> queryByChannelBusinessId(int channelBusinessId);

}
