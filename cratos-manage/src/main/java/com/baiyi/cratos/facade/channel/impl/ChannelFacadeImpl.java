package com.baiyi.cratos.facade.channel.impl;

import com.baiyi.cratos.common.exception.BusinessException;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Channel;
import com.baiyi.cratos.domain.generator.ChannelExtension;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.channel.ChannelExtensionParam;
import com.baiyi.cratos.domain.param.http.channel.ChannelParam;
import com.baiyi.cratos.domain.view.channel.ChannelVO;
import com.baiyi.cratos.eds.aliyun.repo.AliyunDyvmsRepo;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.config.loader.EdsAliyunConfigLoader;
import com.baiyi.cratos.facade.channel.ChannelFacade;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.service.channel.ChannelExtensionService;
import com.baiyi.cratos.service.channel.ChannelService;
import com.baiyi.cratos.wrapper.ChannelWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChannelFacadeImpl implements ChannelFacade {

    private final ChannelService channelService;
    private final ChannelExtensionService channelExtensionService;
    private final ChannelWrapper channelWrapper;
    private final UserService userService;
    private final EdsAliyunConfigLoader edsAliyunConfigLoader;

    @Override
    public DataTable<ChannelVO.Channel> queryChannelPage(ChannelParam.ChannelPageQuery pageQuery) {
        DataTable<Channel> table = channelService.queryChannelPage(pageQuery);
        return channelWrapper.wrapToTarget(table);
    }

    @Override
    public void addChannel(ChannelParam.AddChannel addChannel) {
        channelService.add(addChannel.toTarget());
    }

    @Override
    public void updateChannel(ChannelParam.UpdateChannel updateChannel) {
        channelService.updateByPrimaryKey(updateChannel.toTarget());
    }

    @Override
    public void deleteById(int id) {
        channelService.deleteById(id);
    }

    @Override
    public List<ChannelExtension> queryChannelExtensions(int channelId) {
        return channelExtensionService.queryByChannelId(channelId);
    }

    @Override
    public void addChannelExtension(ChannelExtensionParam.AddChannelExtension addChannelExtension) {
        channelExtensionService.add(addChannelExtension.toTarget());
    }

    @Override
    public void deleteChannelExtensionById(int id) {
        channelExtensionService.deleteById(id);
    }

    @Override
    public void callChannelAlert(ChannelParam.CallAlert callAlert) {
        Channel channel = channelService.getById(callAlert.getChannelId());
        if (channel == null) {
            throw new BusinessException("Channel not found.");
        }
        // 过滤：只呼叫渠道扩展表中的用户
        List<ChannelExtension> extensions = channelExtensionService.queryByChannelId(callAlert.getChannelId());
        Set<String> channelUsernames = extensions.stream()
                .filter(e -> "USER".equals(e.getBusinessType()))
                .map(ChannelExtension::getName)
                .collect(Collectors.toSet());

        List<String> validUsernames = callAlert.getUsernames()
                .stream()
                .filter(channelUsernames::contains)
                .toList();

        if (validUsernames.isEmpty()) {
            throw new BusinessException("No valid users to call.");
        }
        EdsConfigs.Aliyun aliyun = edsAliyunConfigLoader.getConfig(2);
        validUsernames.forEach(username -> {
            User user = userService.getByUsername(username);
            if (user == null || !StringUtils.hasText(user.getMobilePhone())) {
                log.warn("User {} has no mobile phone, skip call.", username);
                return;
            }
            // 这里有bug，给我直接去掉手机号-前面的部分
            String phone = user.getMobilePhone().replaceAll("[\\s+]", "");
            // 去掉国家码前缀（-前面的部分）
            if (phone.contains("-")) {
                phone = phone.substring(phone.lastIndexOf("-") + 1);
            }
            try {
                AliyunDyvmsRepo.callChannelFault(aliyun, channel.getName(), phone);
                log.info("Channel alert call sent to user: {} phone: {}", username, phone);
            } catch (Exception e) {
                log.error("Failed to call user: {} phone: {}, error: {}", username, phone, e.getMessage());
            }
        });
    }

    @Override
    public BaseValidService<?, ?> getValidService() {
        return channelService;
    }

}
