package com.baiyi.cratos.service.channel;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.ChannelLine;
import com.baiyi.cratos.domain.param.http.channel.ChannelLineParam;
import com.baiyi.cratos.mapper.ChannelLineMapper;
import com.baiyi.cratos.service.base.BaseValidService;

import java.util.List;

public interface ChannelLineService extends BaseValidService<ChannelLine, ChannelLineMapper> {

    DataTable<ChannelLine> queryChannelLinePage(ChannelLineParam.ChannelLinePageQuery pageQuery);

    List<ChannelLine> queryByChannelId(int channelId);

}
