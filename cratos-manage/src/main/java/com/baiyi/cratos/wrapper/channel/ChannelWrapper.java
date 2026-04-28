package com.baiyi.cratos.wrapper.channel;

import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Channel;
import com.baiyi.cratos.domain.generator.ChannelExtension;
import com.baiyi.cratos.domain.util.BeanCopierUtils;
import com.baiyi.cratos.domain.view.channel.ChannelVO;
import com.baiyi.cratos.service.channel.ChannelExtensionService;
import com.baiyi.cratos.service.channel.ChannelService;
import com.baiyi.cratos.wrapper.base.BaseBusinessDecorator;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.CHANNEL)
public class ChannelWrapper extends BaseDataTableConverter<ChannelVO.Channel, Channel> implements BaseBusinessDecorator<ChannelVO.HasChannel, ChannelVO.Channel> {

    private final ChannelService channelService;
    private final ChannelExtensionService channelExtensionService;

    @Override
    public void wrap(ChannelVO.Channel vo) {
        List<ChannelExtension> extensions = channelExtensionService.queryByChannelId(vo.getId());
        Map<String, List<ChannelVO.Member>> members = extensions.stream()
                .map(e -> BeanCopierUtils.copyProperties(e, ChannelVO.Member.class))
                .collect(Collectors.groupingBy(ChannelVO.Member::getBusinessType));
        vo.setMembers(members);
    }

    @Override
    public void decorateBusiness(ChannelVO.HasChannel hasBusiness) {
        if (IdentityUtils.hasIdentity(hasBusiness.getChannelId())) {
            com.baiyi.cratos.domain.generator.Channel channel = channelService.getById(hasBusiness.getChannelId());
            if (channel != null) {
                ChannelVO.Channel bVO = this.convert(channel);
                delegateWrap(bVO);
                hasBusiness.setChannel(bVO);
            }
        }
    }
}
