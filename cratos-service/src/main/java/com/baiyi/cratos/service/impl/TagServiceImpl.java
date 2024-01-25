package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.param.tag.TagParam;
import com.baiyi.cratos.mapper.TagMapper;
import com.baiyi.cratos.service.TagService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/2 13:28
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagMapper tagMapper;

    @Override
    public DataTable<Tag> queryPageByParam(TagParam.TagPageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(Tag.class);
        // Example.Criteria criteria = example.createCriteria();
        example.setOrderByClause("create_time");
        List<Tag> data = tagMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

}