package com.baiyi.cratos.facade.channel;

import com.baiyi.cratos.HasSetValid;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.channel.ChannelBusinessNodeParam;
import com.baiyi.cratos.domain.param.http.channel.ChannelNodeParam;
import com.baiyi.cratos.domain.view.channel.ChannelNodeVO;

import java.util.List;

public interface ChannelNodeFacade extends HasSetValid {

    DataTable<ChannelNodeVO.Node> queryChannelNodePage(ChannelNodeParam.ChannelNodePageQuery pageQuery);

    void addChannelNode(ChannelNodeParam.AddChannelNode addChannelNode);

    void updateChannelNode(ChannelNodeParam.UpdateChannelNode updateChannelNode);

    void deleteById(int id);

    void addChannelBusinessNode(ChannelBusinessNodeParam.AddChannelBusinessNode param);

    void deleteChannelBusinessNodeById(int id);

    List<ChannelNodeVO.Node> queryChannelBusinessNodes(int channelBusinessId);

}
