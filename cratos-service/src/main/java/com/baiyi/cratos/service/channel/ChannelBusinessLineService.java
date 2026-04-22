package com.baiyi.cratos.service.channel;

import com.baiyi.cratos.domain.generator.ChannelBusinessLine;
import com.baiyi.cratos.mapper.ChannelBusinessLineMapper;
import com.baiyi.cratos.service.base.BaseValidService;

import java.util.List;

public interface ChannelBusinessLineService extends BaseValidService<ChannelBusinessLine, ChannelBusinessLineMapper> {

    List<ChannelBusinessLine> queryByChannelBusinessId(int channelBusinessId);

}
