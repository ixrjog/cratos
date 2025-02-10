package com.baiyi.cratos.facade;

import com.baiyi.cratos.HasSetValid;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.channel.ChannelNetworkParam;
import com.baiyi.cratos.domain.view.channel.ChannelNetworkVO;

/**
 * @Author baiyi
 * @Date 2024/2/21 11:17
 * @Version 1.0
 */
public interface ChannelNetworkFacade extends HasSetValid {

    DataTable<ChannelNetworkVO.ChannelNetwork> queryChannelNetworkPage(
            ChannelNetworkParam.ChannelNetworkPageQuery pageQuery);

    void addChannelNetwork(ChannelNetworkParam.AddChannelNetwork addChannelNetwork);

    void updateChannelNetwork(ChannelNetworkParam.UpdateChannelNetwork updateChannelNetwork);

    void deleteById(int id);

}
