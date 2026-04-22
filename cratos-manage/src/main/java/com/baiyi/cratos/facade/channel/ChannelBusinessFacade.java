package com.baiyi.cratos.facade.channel;

import com.baiyi.cratos.HasSetValid;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.channel.ChannelBusinessParam;
import com.baiyi.cratos.domain.view.channel.ChannelBusinessVO;

public interface ChannelBusinessFacade extends HasSetValid {

    DataTable<ChannelBusinessVO.Business> queryChannelBusinessPage(ChannelBusinessParam.ChannelBusinessPageQuery pageQuery);

    void addChannelBusiness(ChannelBusinessParam.AddChannelBusiness addChannelBusiness);

    void updateChannelBusiness(ChannelBusinessParam.UpdateChannelBusiness updateChannelBusiness);

    void deleteById(int id);

}
