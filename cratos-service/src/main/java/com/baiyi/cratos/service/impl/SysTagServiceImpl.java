package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.SysTag;
import com.baiyi.cratos.domain.param.tag.TagParam;
import com.baiyi.cratos.mapper.SysTagMapper;
import com.baiyi.cratos.service.SysTagService;
import com.baiyi.cratos.service.base.AbstractService;
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
public class SysTagServiceImpl extends AbstractService<SysTag, SysTagMapper> implements SysTagService {

    private final SysTagMapper sysTagMapper;

    @Override
    protected SysTagMapper getMapper() {
        return sysTagMapper;
    }

    @Override
    public DataTable<SysTag> queryPageByParam(TagParam.TagPageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(SysTag.class);
        Example.Criteria criteria = example.createCriteria();
        example.setOrderByClause("create_time");
        List<SysTag> data = sysTagMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

}