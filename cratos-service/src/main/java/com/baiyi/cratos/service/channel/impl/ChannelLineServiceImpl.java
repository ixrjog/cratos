package com.baiyi.cratos.service.channel.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.ChannelLine;
import com.baiyi.cratos.domain.param.http.channel.ChannelLineParam;
import com.baiyi.cratos.mapper.ChannelLineMapper;
import com.baiyi.cratos.service.channel.ChannelLineService;
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
public class ChannelLineServiceImpl implements ChannelLineService {
    private final ChannelLineMapper channelLineMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:CHANNELLINE:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public DataTable<ChannelLine> queryChannelLinePage(ChannelLineParam.ChannelLinePageQuery pageQuery) {
        Page<ChannelLine> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(ChannelLine.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.hasText(pageQuery.getQueryName())) {
            criteria.andLike("name", SqlUtils.ofLike(pageQuery.getQueryName()
                                                             .trim())
            );
        }
        if (pageQuery.getChannelId() != null) {
            criteria.andEqualTo("channelId", pageQuery.getChannelId());
        }
        List<ChannelLine> data = channelLineMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public List<ChannelLine> queryByChannelId(int channelId) {
        Example example = new Example(ChannelLine.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("channelId", channelId);
        return channelLineMapper.selectByExample(example);
    }

}
