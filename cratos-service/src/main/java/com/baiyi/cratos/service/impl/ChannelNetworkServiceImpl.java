package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.ChannelNetwork;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.param.channel.ChannelNetworkParam;
import com.baiyi.cratos.mapper.ChannelNetworkMapper;
import com.baiyi.cratos.service.ChannelNetworkService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/2/21 11:19
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.CHANNEL_NETWORK)
public class ChannelNetworkServiceImpl implements ChannelNetworkService {

    private final ChannelNetworkMapper channelNetworkMapper;

    @Override
    public DataTable<ChannelNetwork> queryChannelNetworkPage(ChannelNetworkParam.ChannelNetworkPageQuery pageQuery) {
        Page<ChannelNetwork> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<ChannelNetwork> data = channelNetworkMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

}
