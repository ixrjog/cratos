package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.annotation.DeleteBoundBusiness;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.RiskEvent;
import com.baiyi.cratos.domain.param.risk.RiskEventParam;
import com.baiyi.cratos.mapper.RiskEventMapper;
import com.baiyi.cratos.service.RiskEventService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/4/15 下午3:19
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class RiskEventServiceImpl implements RiskEventService {

    private final RiskEventMapper riskEventMapper;

    @Override
    public RiskEvent getByUniqueKey(RiskEvent riskEvent) {
        Example example = new Example(RiskEvent.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", riskEvent.getName());
        return riskEventMapper.selectOneByExample(example);
    }

    @Override
    public DataTable<RiskEvent> queryRiskEventPage(RiskEventParam.RiskEventPageQuery pageQuery) {
        Page<RiskEvent> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<RiskEvent> data = riskEventMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public List<String> queryYears() {
        return riskEventMapper.queryYears();
    }

    @Override
    @DeleteBoundBusiness(businessId = "#id", targetTypes = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC})
    public void deleteById(int id) {
        riskEventMapper.deleteByPrimaryKey(id);
    }

}
