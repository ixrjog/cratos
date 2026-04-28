package com.baiyi.cratos.service.channel.impl;

import com.baiyi.cratos.domain.generator.ChannelBusinessNode;
import com.baiyi.cratos.mapper.ChannelBusinessNodeMapper;
import com.baiyi.cratos.service.channel.ChannelBusinessNodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

@Service
@RequiredArgsConstructor
public class ChannelBusinessNodeServiceImpl implements ChannelBusinessNodeService {

    private final ChannelBusinessNodeMapper channelBusinessNodeMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:CHANNELBUSINESSNODE:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public List<ChannelBusinessNode> queryByChannelBusinessId(int channelBusinessId) {
        Example example = new Example(ChannelBusinessNode.class);
        example.createCriteria()
                .andEqualTo("channelBusinessId", channelBusinessId);
        return channelBusinessNodeMapper.selectByExample(example);
    }

    @Override
    public List<ChannelBusinessNode> queryByChannelNodeId(int channelNodeId) {
        Example example = new Example(ChannelBusinessNode.class);
        example.createCriteria()
                .andEqualTo("channelNodeId", channelNodeId);
        return channelBusinessNodeMapper.selectByExample(example);
    }

    @Override
    public ChannelBusinessNode getByUniqueKey(@NonNull ChannelBusinessNode record) {
        Example example = new Example(ChannelBusinessNode.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("channelBusinessId", record.getChannelBusinessId())
                .andEqualTo("channelNodeId", record.getChannelNodeId());
        return channelBusinessNodeMapper.selectOneByExample(example);
    }

}
