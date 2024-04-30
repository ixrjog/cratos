package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.ChannelNetwork;
import com.baiyi.cratos.domain.param.channel.ChannelNetworkParam;
import com.baiyi.cratos.mapper.ChannelNetworkMapper;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.service.base.SupportBusinessService;

/**
 * @Author baiyi
 * @Date 2024/2/21 11:19
 * @Version 1.0
 */
public interface ChannelNetworkService extends BaseValidService<ChannelNetwork, ChannelNetworkMapper>, SupportBusinessService {

    DataTable<ChannelNetwork> queryChannelNetworkPage(ChannelNetworkParam.ChannelNetworkPageQueryParam param);

}
