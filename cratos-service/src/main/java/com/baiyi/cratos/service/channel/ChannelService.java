package com.baiyi.cratos.service.channel;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Channel;
import com.baiyi.cratos.domain.param.http.channel.ChannelParam;
import com.baiyi.cratos.mapper.ChannelMapper;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.service.base.SupportBusinessService;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/20 17:09
 * &#064;Version 1.0
 */
public interface ChannelService extends BaseValidService<Channel, ChannelMapper>, SupportBusinessService {

    DataTable<Channel> queryChannelPage(ChannelParam.ChannelPageQuery pageQuery);

}
