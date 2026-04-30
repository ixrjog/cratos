package com.baiyi.cratos.service.channel.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Channel;
import com.baiyi.cratos.domain.param.http.channel.ChannelParam;
import com.baiyi.cratos.mapper.ChannelMapper;
import com.baiyi.cratos.service.channel.ChannelService;
import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.util.SqlUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.CHANNEL)
public class ChannelServiceImpl implements ChannelService {

    private final ChannelMapper channelMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:CHANNEL:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public DataTable<Channel> queryChannelPage(ChannelParam.ChannelPageQuery pageQuery) {
        Page<Channel> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(Channel.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.hasText(pageQuery.getQueryName())) {
            criteria.andLike("name", SqlUtils.ofLike(pageQuery.getQueryName().trim()));
        }
        if (StringUtils.hasText(pageQuery.getCountry())) {
            criteria.andEqualTo("country", pageQuery.getCountry());
        }
        List<Channel> data = channelMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public OptionsVO.Options queryCountryOptions() {
        List<Channel> all = channelMapper.selectAll();
        Map<String, Long> countMap = all.stream()
                .filter(c -> StringUtils.hasText(c.getCountry()))
                .collect(Collectors.groupingBy(Channel::getCountry, Collectors.counting()));
        List<OptionsVO.Option> options = countMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(e -> OptionsVO.Option.builder()
                        .label(e.getKey())
                        .value(e.getKey())
                        .comment(e.getValue())
                        .build())
                .collect(Collectors.toList());
        return OptionsVO.Options.builder().options(options).build();
    }

}
