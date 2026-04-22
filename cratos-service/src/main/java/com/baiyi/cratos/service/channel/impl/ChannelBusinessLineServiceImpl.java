package com.baiyi.cratos.service.channel.impl;

import com.baiyi.cratos.domain.generator.ChannelBusinessLine;
import com.baiyi.cratos.mapper.ChannelBusinessLineMapper;
import com.baiyi.cratos.service.channel.ChannelBusinessLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

@Service
@RequiredArgsConstructor
public class ChannelBusinessLineServiceImpl implements ChannelBusinessLineService {
    private final ChannelBusinessLineMapper channelBusinessLineMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:CHANNELBUSINESSLINE:ID:' + #id")
    public void clearCacheById(int id) {}

    @Override
    public List<ChannelBusinessLine> queryByChannelBusinessId(int channelBusinessId) {
        Example example = new Example(ChannelBusinessLine.class);
        example.createCriteria().andEqualTo("channelBusinessId", channelBusinessId);
        return channelBusinessLineMapper.selectByExample(example);
    }
}
