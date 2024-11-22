package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.ChannelNetwork;
import com.baiyi.cratos.domain.param.http.channel.ChannelNetworkParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ChannelNetworkMapper extends Mapper<ChannelNetwork> {

    List<ChannelNetwork> queryPageByParam(ChannelNetworkParam.ChannelNetworkPageQueryParam param);

}