package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.ChannelNetwork;
import com.baiyi.cratos.domain.view.channel.ChannelNetworkVO;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/2/21 11:36
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ChannelNetworkWrapper extends BaseDataTableConverter<ChannelNetworkVO.ChannelNetwork, ChannelNetwork> implements IBaseWrapper<ChannelNetworkVO.ChannelNetwork> {

    @Override
    @BusinessWrapper(types = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC})
    public void wrap(ChannelNetworkVO.ChannelNetwork channelNetwork) {
        // This is a good idea
    }

}