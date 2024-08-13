package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.eds.EdsInstanceParam;
import com.baiyi.cratos.mapper.EdsInstanceMapper;
import com.baiyi.cratos.service.EdsInstanceService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/2/5 16:53
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.EDS_INSTANCE)
public class EdsInstanceServiceImpl implements EdsInstanceService {

    private final EdsInstanceMapper edsInstanceMapper;

    @Override
    public DataTable<EdsInstance> queryEdsInstancePage(EdsInstanceParam.InstancePageQuery pageQuery) {
        Page<EdsInstance> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<EdsInstance> data = edsInstanceMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public EdsInstance getByUniqueKey(@NonNull EdsInstance record) {
        Example example = new Example(EdsInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceName", record.getInstanceName());
        return edsInstanceMapper.selectOneByExample(example);
    }

    @Override
    public int selectCountByConfigId(Integer configId) {
        Example example = new Example(EdsInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("configId", configId);
        return edsInstanceMapper.selectCountByExample(example);
    }

    @Override
    public List<EdsInstance> queryValidEdsInstanceByType(String edsType) {
        Example example = new Example(EdsInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("edsType", edsType)
                .andEqualTo("valid", true);
        return edsInstanceMapper.selectByExample(example);
    }

}
