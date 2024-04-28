package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.annotation.DeleteBoundBusiness;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Domain;
import com.baiyi.cratos.domain.param.domain.DomainParam;
import com.baiyi.cratos.mapper.DomainMapper;
import com.baiyi.cratos.service.DomainService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/4/28 上午9:58
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.DOMAIN)
public class DomainServiceImpl implements DomainService {

    private final DomainMapper domainMapper;

    @Override
    public DataTable<Domain> queryDomainPage(DomainParam.DomainPageQuery pageQuery) {
        Page<Domain> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<Domain> data = domainMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public Domain getByUniqueKey(Domain domain) {
        Example example = new Example(Domain.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", domain.getName());
        return domainMapper.selectOneByExample(example);
    }

    @Override
    @DeleteBoundBusiness(businessId = "#id", targetTypes = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC})
    public void deleteById(int id) {
        domainMapper.deleteByPrimaryKey(id);
    }

}
