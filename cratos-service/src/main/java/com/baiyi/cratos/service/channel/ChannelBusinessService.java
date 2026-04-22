package com.baiyi.cratos.service.channel;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.ChannelBusiness;
import com.baiyi.cratos.domain.param.http.channel.ChannelBusinessParam;
import com.baiyi.cratos.mapper.ChannelBusinessMapper;
import com.baiyi.cratos.service.base.BaseValidService;

public interface ChannelBusinessService extends BaseValidService<ChannelBusiness, ChannelBusinessMapper> {

    DataTable<ChannelBusiness> queryChannelBusinessPage(ChannelBusinessParam.ChannelBusinessPageQuery pageQuery);

}
