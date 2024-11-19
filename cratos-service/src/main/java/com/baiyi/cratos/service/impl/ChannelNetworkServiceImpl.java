package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.annotation.DeleteBoundBusiness;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.ChannelNetwork;
import com.baiyi.cratos.domain.param.channel.ChannelNetworkParam;
import com.baiyi.cratos.mapper.ChannelNetworkMapper;
import com.baiyi.cratos.service.ChannelNetworkService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

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
    public DataTable<ChannelNetwork> queryChannelNetworkPage(ChannelNetworkParam.ChannelNetworkPageQueryParam param){
        Page<ChannelNetwork> page = PageHelper.startPage(param.getPage(), param.getLength());
        List<ChannelNetwork> data = channelNetworkMapper.queryPageByParam(param);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    @DeleteBoundBusiness(businessId = "#id", targetTypes = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC})
    public void deleteById(int id) {
        ChannelNetworkService.super.deleteById(id);
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:CHANNELNETWORK:ID:' + #id")
    public void clearCacheById(int id) {
    }

}
