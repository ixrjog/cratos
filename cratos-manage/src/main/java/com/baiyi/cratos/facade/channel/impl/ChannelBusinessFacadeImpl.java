package com.baiyi.cratos.facade.channel.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.ChannelBusiness;
import com.baiyi.cratos.domain.generator.ChannelBusinessNode;
import com.baiyi.cratos.domain.param.http.channel.ChannelBusinessParam;
import com.baiyi.cratos.domain.view.channel.ChannelBusinessVO;
import com.baiyi.cratos.facade.channel.ChannelBusinessFacade;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.service.channel.ChannelBusinessNodeService;
import com.baiyi.cratos.service.channel.ChannelBusinessService;
import com.baiyi.cratos.wrapper.channel.ChannelBusinessWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChannelBusinessFacadeImpl implements ChannelBusinessFacade {

    private final ChannelBusinessService channelBusinessService;
    private final ChannelBusinessNodeService channelBusinessNodeService;
    private final ChannelBusinessWrapper channelBusinessWrapper;

    @Override
    public DataTable<ChannelBusinessVO.Business> queryChannelBusinessPage(
            ChannelBusinessParam.ChannelBusinessPageQuery pageQuery) {
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
        // 删除关联关系
        List<ChannelBusinessNode> nodes = channelBusinessNodeService.queryByChannelBusinessId(id);
        if (!CollectionUtils.isEmpty(nodes)) {
            nodes.forEach(channelBusinessNode -> channelBusinessNodeService.deleteById(channelBusinessNode.getId()));
        }
        channelBusinessService.deleteById(id);
    }

    @Override
    public BaseValidService<?, ?> getValidService() {
        return channelBusinessService;
    }

}
