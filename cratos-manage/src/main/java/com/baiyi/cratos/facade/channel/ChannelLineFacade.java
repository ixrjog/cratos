package com.baiyi.cratos.facade.channel;

import com.baiyi.cratos.HasSetValid;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.channel.ChannelBusinessLineParam;
import com.baiyi.cratos.domain.param.http.channel.ChannelLineParam;
import com.baiyi.cratos.domain.view.channel.ChannelLineVO;

import java.util.List;

public interface ChannelLineFacade extends HasSetValid {

    DataTable<ChannelLineVO.Line> queryChannelLinePage(ChannelLineParam.ChannelLinePageQuery pageQuery);

    void addChannelLine(ChannelLineParam.AddChannelLine addChannelLine);

    void updateChannelLine(ChannelLineParam.UpdateChannelLine updateChannelLine);

    void deleteById(int id);

    void addChannelBusinessLine(ChannelBusinessLineParam.AddChannelBusinessLine param);

    void deleteChannelBusinessLineById(int id);

    List<ChannelLineVO.Line> queryChannelBusinessLines(int channelBusinessId);

}
