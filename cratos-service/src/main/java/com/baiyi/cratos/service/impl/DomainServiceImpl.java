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
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
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
    public DataTable<Domain> queryDomainPage(DomainParam.DomainPageQueryParam param) {
        Page<Domain> page = PageHelper.startPage(param.getPage(), param.getLength());
        List<Domain> data = domainMapper.queryPageByParam(param);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public Domain getByUniqueKey(@NonNull Domain record) {
        Example example = new Example(Domain.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", record.getName());
        return domainMapper.selectOneByExample(example);
    }

    @Override
    public List<Domain> queryByLessThanExpiry(Date date) {
        Example example = new Example(Domain.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLessThan("expiry", date)
                .andEqualTo("valid", true);
        return domainMapper.selectByExample(example);
    }

    @Override
    @DeleteBoundBusiness(businessId = "#id", targetTypes = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC})
    public void deleteById(int id) {
        domainMapper.deleteByPrimaryKey(id);
    }

}
