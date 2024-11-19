package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.annotation.DeleteBoundBusiness;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.RiskEvent;
import com.baiyi.cratos.domain.param.risk.RiskEventParam;
import com.baiyi.cratos.domain.view.base.GraphVO;
import com.baiyi.cratos.mapper.RiskEventMapper;
import com.baiyi.cratos.service.RiskEventService;
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
 * @Author baiyi
 * @Date 2024/4/15 下午3:19
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.RISK_EVENT)
public class RiskEventServiceImpl implements RiskEventService {

    private final RiskEventMapper riskEventMapper;

    @Override
    public RiskEvent getByUniqueKey(@NonNull RiskEvent record) {
        Example example = new Example(RiskEvent.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", record.getName());
        return riskEventMapper.selectOneByExample(example);
    }

    @Override
    public DataTable<RiskEvent> queryRiskEventPage(RiskEventParam.RiskEventPageQueryParam param) {
        Page<RiskEvent> page = PageHelper.startPage(param.getPage(), param.getLength());
        List<RiskEvent> data = riskEventMapper.queryPageByParam(param);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public List<String> queryYears() {
        return riskEventMapper.queryYears();
    }

    @Override
    @DeleteBoundBusiness(businessId = "#id", targetTypes = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC})
    public void deleteById(int id) {
        RiskEventService.super.deleteById(id);
    }

    @Override
    public List<GraphVO.SimpleData> querySLADataForTheMonth(RiskEventParam.RiskEventGraphQuery riskEventGraphQuery,
                                                            List<Integer> impactIdList) {
        return riskEventMapper.querySLADataForTheMonth(riskEventGraphQuery.getYear(), riskEventGraphQuery.getQuarter(),
                impactIdList);
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:RISKEVENT:ID:' + #id")
    public void clearCacheById(int id) {
    }

}
