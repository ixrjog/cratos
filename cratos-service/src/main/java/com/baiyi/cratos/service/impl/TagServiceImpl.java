package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.param.http.tag.TagParam;
import com.baiyi.cratos.mapper.TagMapper;
import com.baiyi.cratos.service.TagService;
import com.baiyi.cratos.util.SqlHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.*;

/**
 * @Author baiyi
 * @Date 2024/1/2 13:28
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagMapper tagMapper;

    public static final String GROUP = "Group";
    public static final String ENV = "Env";

    @Override
    public DataTable<Tag> queryPageByParam(TagParam.TagPageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(Tag.class);
        if (StringUtils.isNotBlank(pageQuery.getTagKey())) {
            Example.Criteria criteria = example.createCriteria();
            criteria.andLike("tagKey", SqlHelper.toLike(pageQuery.getTagKey()));
        }
        example.setOrderByClause("seq");
        List<Tag> data = tagMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    @Cacheable(cacheNames = VERY_SHORT, key = "'DOMAIN:TAG:KEY:'+ #tagKey", unless = "#result == null")
    public Tag getByTagKey(String tagKey) {
        Tag uniqueKey = Tag.builder()
                .tagKey(tagKey)
                .build();
        return getByUniqueKey(uniqueKey);
    }

    @Override
    public Tag getByUniqueKey(@NonNull Tag record) {
        Example example = new Example(Tag.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("tagKey", record.getTagKey());
        return getMapper().selectOneByExample(example);
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:TAG:ID:' + #id")
    public void clearCacheById(int id) {
    }

}