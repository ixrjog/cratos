package com.baiyi.cratos.service.channel.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.ChannelBusiness;
import com.baiyi.cratos.domain.param.http.channel.ChannelBusinessParam;
import com.baiyi.cratos.mapper.ChannelBusinessMapper;
import com.baiyi.cratos.service.channel.ChannelBusinessService;
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
public class ChannelBusinessServiceImpl implements ChannelBusinessService {

    private final ChannelBusinessMapper channelBusinessMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:CHANNELBUSINESS:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public DataTable<ChannelBusiness> queryChannelBusinessPage(ChannelBusinessParam.ChannelBusinessPageQuery pageQuery) {
        Page<ChannelBusiness> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(ChannelBusiness.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.hasText(pageQuery.getQueryName())) {
            criteria.andLike("businessName", SqlUtils.ofLike(pageQuery.getQueryName().trim()));
        }
        if (pageQuery.getChannelId() != null) {
            criteria.andEqualTo("channelId", pageQuery.getChannelId());
        }
        if (pageQuery.getOrganizationId() != null) {
            criteria.andEqualTo("organizationId", pageQuery.getOrganizationId());
        }
        List<ChannelBusiness> data = channelBusinessMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

}
