package com.baiyi.cratos.service.work.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.WorkOrderGroup;
import com.baiyi.cratos.domain.param.http.work.WorkOrderParam;
import com.baiyi.cratos.mapper.WorkOrderGroupMapper;
import com.baiyi.cratos.service.work.WorkOrderGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/17 13:39
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
public class WorkOrderGroupServiceImpl implements WorkOrderGroupService {

    private final WorkOrderGroupMapper workOrderGroupMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:WORKORDER_GROUP:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public WorkOrderGroup getByUniqueKey(@NonNull WorkOrderGroup record) {
        Example example = new Example(WorkOrderGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", record.getName());
        return workOrderGroupMapper.selectOneByExample(example);
    }

    @Override
    public DataTable<WorkOrderGroup> queryPageByParam(WorkOrderParam.GroupPageQuery pageQuery) {
        Page<WorkOrderGroup> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<WorkOrderGroup> data = workOrderGroupMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

}
