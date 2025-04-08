package com.baiyi.cratos.service.work.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.param.http.work.WorkOrderParam;
import com.baiyi.cratos.mapper.WorkOrderMapper;
import com.baiyi.cratos.service.work.WorkOrderService;
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
 * &#064;Date  2025/3/17 11:45
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.WORKORDER)
public class WorkOrderServiceImpl implements WorkOrderService {

    private final WorkOrderMapper workOrderMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:WORKORDER:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public WorkOrder getByUniqueKey(@NonNull WorkOrder record) {
        Example example = new Example(WorkOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workOrderKey", record.getWorkOrderKey());
        return workOrderMapper.selectOneByExample(example);
    }

    @Override
    public List<WorkOrder> queryByGroupId(int groupId) {
        Example example = new Example(WorkOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("groupId", groupId);
        return workOrderMapper.selectByExample(example);
    }

    @Override
    public DataTable<WorkOrder> queryPageByParam(WorkOrderParam.WorkOrderPageQuery pageQuery) {
        Page<WorkOrder> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<WorkOrder> data = getMapper().queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

}
