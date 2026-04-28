package com.baiyi.cratos.service.channel.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.ChannelNode;
import com.baiyi.cratos.domain.param.http.channel.ChannelNodeParam;
import com.baiyi.cratos.mapper.ChannelNodeMapper;
import com.baiyi.cratos.service.channel.ChannelNodeService;
import com.baiyi.cratos.util.SqlUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

@Service
@RequiredArgsConstructor
public class ChannelNodeServiceImpl implements ChannelNodeService {
    private final ChannelNodeMapper channelNodeMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:CHANNELNODE:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public DataTable<ChannelNode> queryChannelNodePage(ChannelNodeParam.ChannelNodePageQuery pageQuery) {
        Page<ChannelNode> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(ChannelNode.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.hasText(pageQuery.getQueryName())) {
            criteria.andLike("name", SqlUtils.ofLike(pageQuery.getQueryName()
                                                             .trim())
            );
        }
        if (pageQuery.getChannelId() != null) {
            criteria.andEqualTo("channelId", pageQuery.getChannelId());
        }
        example.setOrderByClause("seq");
        List<ChannelNode> data = channelNodeMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public List<ChannelNode> queryByChannelId(int channelId) {
        Example example = new Example(ChannelNode.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("channelId", channelId);
        return channelNodeMapper.selectByExample(example);
    }

}
