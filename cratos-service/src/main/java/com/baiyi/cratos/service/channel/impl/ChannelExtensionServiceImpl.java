package com.baiyi.cratos.service.channel.impl;

import com.baiyi.cratos.domain.generator.ChannelExtension;
import com.baiyi.cratos.mapper.ChannelExtensionMapper;
import com.baiyi.cratos.service.channel.ChannelExtensionService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

@Service
@RequiredArgsConstructor
public class ChannelExtensionServiceImpl implements ChannelExtensionService {

    private final ChannelExtensionMapper channelExtensionMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:CHANNELEXTENSION:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public List<ChannelExtension> queryByChannelId(int channelId) {
        Example example = new Example(ChannelExtension.class);
        example.createCriteria().andEqualTo("channelId", channelId);
        return channelExtensionMapper.selectByExample(example);
    }

    @Override
    public ChannelExtension getByUniqueKey(@NonNull ChannelExtension record) {
        Example example = new Example(ChannelExtension.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("channelId", record.getChannelId())
                .andEqualTo("businessType", record.getBusinessType())
                .andEqualTo("businessId", record.getBusinessId())
                .andEqualTo("role", record.getRole())
                .andEqualTo("name", record.getName());
        return channelExtensionMapper.selectOneByExample(example);
    }

}
