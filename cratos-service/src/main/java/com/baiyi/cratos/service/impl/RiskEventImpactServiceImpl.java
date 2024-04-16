package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.annotation.DeleteBoundBusiness;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.RiskEventImpact;
import com.baiyi.cratos.mapper.RiskEventImpactMapper;
import com.baiyi.cratos.service.RiskEventImpactService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/4/15 下午5:00
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.RISK_EVENT_IMPACT)
public class RiskEventImpactServiceImpl implements RiskEventImpactService {

    private final RiskEventImpactMapper riskEventImpactMapper;

    @Override
    public List<RiskEventImpact> queryByEventId(int eventId) {
        Example example = new Example(RiskEventImpact.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("riskEventId", eventId);
        return riskEventImpactMapper.selectByExample(example);
    }

    @Override
    @DeleteBoundBusiness(businessId = "#id", targetTypes = {BusinessTypeEnum.BUSINESS_TAG})
    public void deleteById(int id) {
        riskEventImpactMapper.deleteByPrimaryKey(id);
    }

}
