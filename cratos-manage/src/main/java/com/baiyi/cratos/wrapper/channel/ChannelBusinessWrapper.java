package com.baiyi.cratos.wrapper.channel;

import com.baiyi.cratos.annotation.BusinessDecorator;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.ChannelBusiness;
import com.baiyi.cratos.domain.view.channel.ChannelBusinessVO;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.BaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChannelBusinessWrapper extends BaseDataTableConverter<ChannelBusinessVO.Business, ChannelBusiness> implements BaseWrapper<ChannelBusinessVO.Business> {

    @Override
    @BusinessDecorator(types = {BusinessTypeEnum.CHANNEL, BusinessTypeEnum.CHANNEL_LINE, BusinessTypeEnum.ORGANIZATION, BusinessTypeEnum.ACCOUNT_ENTITY})
    public void wrap(ChannelBusinessVO.Business vo) {
    }

}
