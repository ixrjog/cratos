package com.baiyi.cratos.facade.channel.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.ChannelLine;
import com.baiyi.cratos.domain.param.http.channel.ChannelBusinessLineParam;
import com.baiyi.cratos.domain.param.http.channel.ChannelLineParam;
import com.baiyi.cratos.domain.util.BeanCopierUtils;
import com.baiyi.cratos.domain.view.channel.ChannelLineVO;
import com.baiyi.cratos.facade.channel.ChannelLineFacade;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.service.channel.ChannelBusinessLineService;
import com.baiyi.cratos.service.channel.ChannelLineService;
import com.baiyi.cratos.wrapper.ChannelLineWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChannelLineFacadeImpl implements ChannelLineFacade {
    private final ChannelLineService channelLineService;
    private final ChannelBusinessLineService channelBusinessLineService;
    private final ChannelLineWrapper channelLineWrapper;

    @Override
    public DataTable<ChannelLineVO.Line> queryChannelLinePage(ChannelLineParam.ChannelLinePageQuery pageQuery) {
        DataTable<ChannelLine> table = channelLineService.queryChannelLinePage(pageQuery);
        return channelLineWrapper.wrapToTarget(table);
    }

    @Override
    public void addChannelLine(ChannelLineParam.AddChannelLine addChannelLine) {
        channelLineService.add(addChannelLine.toTarget());
    }
 
    @Override
    public void updateChannelLine(ChannelLineParam.UpdateChannelLine updateChannelLine) {
        channelLineService.updateByPrimaryKey(updateChannelLine.toTarget());
    }

    @Override
    public void deleteById(int id) {
        channelLineService.deleteById(id);
    }

    @Override
    public void addChannelBusinessLine(ChannelBusinessLineParam.AddChannelBusinessLine param) {
        channelBusinessLineService.add(param.toTarget());
    }

    @Override
    public void deleteChannelBusinessLineById(int id) {
        channelBusinessLineService.deleteById(id);
    }

    @Override
    public List<ChannelLineVO.Line> queryChannelBusinessLines(int channelBusinessId) {
        return channelBusinessLineService.queryByChannelBusinessId(channelBusinessId)
                .stream()
                .map(e -> BeanCopierUtils.copyProperties(
                        channelLineService.getById(e.getChannelLineId()), ChannelLineVO.Line.class))
                .toList();
    }

    @Override
    public BaseValidService<?, ?> getValidService() {
        return channelLineService;
    }
}
