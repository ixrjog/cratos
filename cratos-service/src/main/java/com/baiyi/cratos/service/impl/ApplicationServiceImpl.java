package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.param.application.ApplicationParam;
import com.baiyi.cratos.mapper.ApplicationMapper;
import com.baiyi.cratos.service.ApplicationService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/15 11:18
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.APPLICATION)
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationMapper applicationMapper;

    @Override
    public DataTable<Application> queryApplicationPage(ApplicationParam.ApplicationPageQueryParam param) {
        Page<Application> page = PageHelper.startPage(param.getPage(), param.getLength());
        List<Application> data = applicationMapper.queryPageByParam(param);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public Application getByUniqueKey(@NonNull Application record) {
        Example example = new Example(Application.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", record.getName());
        return applicationMapper.selectOneByExample(example);
    }

}
