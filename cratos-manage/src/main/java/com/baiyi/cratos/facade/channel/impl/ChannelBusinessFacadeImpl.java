package com.baiyi.cratos.facade.channel.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.ChannelBusiness;
import com.baiyi.cratos.domain.param.http.channel.ChannelBusinessParam;
import com.baiyi.cratos.domain.view.channel.ChannelBusinessVO;
import com.baiyi.cratos.facade.channel.ChannelBusinessFacade;
import com.baiyi.cratos.service.channel.ChannelBusinessService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.wrapper.ChannelBusinessWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChannelBusinessFacadeImpl implements ChannelBusinessFacade {

    private final ChannelBusinessService channelBusinessService;
    private final ChannelBusinessWrapper channelBusinessWrapper;

    @Override
    public DataTable<ChannelBusinessVO.Business> queryChannelBusinessPage(ChannelBusinessParam.ChannelBusinessPageQuery pageQuery) {
        DataTable<ChannelBusiness> table = channelBusinessService.queryChannelBusinessPage(pageQuery);
        return channelBusinessWrapper.wrapToTarget(table);
    }

    @Override
    public void addChannelBusiness(ChannelBusinessParam.AddChannelBusiness addChannelBusiness) {
        channelBusinessService.add(addChannelBusiness.toTarget());
    }

    @Override
    public void updateChannelBusiness(ChannelBusinessParam.UpdateChannelBusiness updateChannelBusiness) {
        channelBusinessService.updateByPrimaryKey(updateChannelBusiness.toTarget());
    }

    @Override
    public void deleteById(int id) {
        channelBusinessService.deleteById(id);
    }

    @Override
    public BaseValidService<?, ?> getValidService() {
        return channelBusinessService;
    }

}
