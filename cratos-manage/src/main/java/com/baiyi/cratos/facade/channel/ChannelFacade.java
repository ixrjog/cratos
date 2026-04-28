package com.baiyi.cratos.facade.channel;

import com.baiyi.cratos.HasSetValid;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.channel.ChannelExtensionParam;
import com.baiyi.cratos.domain.param.http.channel.ChannelParam;
import com.baiyi.cratos.domain.view.channel.ChannelExtensionVO;
import com.baiyi.cratos.domain.view.channel.ChannelVO;

import java.util.List;

public interface ChannelFacade extends HasSetValid {

    DataTable<ChannelVO.Channel> queryChannelPage(ChannelParam.ChannelPageQuery pageQuery);

    void addChannel(ChannelParam.AddChannel addChannel);

    void updateChannel(ChannelParam.UpdateChannel updateChannel);

    void deleteById(int id);

    List<ChannelExtensionVO.Extension> queryChannelExtensions(int channelId);

    void addChannelExtension(ChannelExtensionParam.AddChannelExtension addChannelExtension);

    void deleteChannelExtensionById(int id);

    void callChannelAlert(ChannelParam.CallAlert callAlert);

}
