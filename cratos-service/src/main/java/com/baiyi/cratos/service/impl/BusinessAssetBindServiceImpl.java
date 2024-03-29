package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.generator.BusinessAssetBind;
import com.baiyi.cratos.mapper.BusinessAssetBindMapper;
import com.baiyi.cratos.service.BusinessAssetBindService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * @Author baiyi
 * @Date 2024/3/12 09:47
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BusinessAssetBindServiceImpl implements BusinessAssetBindService {

    private final BusinessAssetBindMapper businessAssetBindMapper;

    @Override
    public void deleteByBusiness(BaseBusiness.IBusiness business) {
        Example example = new Example(BusinessAssetBind.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", business.getBusinessType())
                .andEqualTo("businessId", business.getBusinessId());
        businessAssetBindMapper.deleteByExample(example);
    }

    @Override
    public BusinessAssetBind getByUniqueKey(BusinessAssetBind businessAssetBind) {
        Example example = new Example(BusinessAssetBind.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", businessAssetBind.getBusinessType())
                .andEqualTo("assetId", businessAssetBind.getAssetId());
        return businessAssetBindMapper.selectOneByExample(example);
    }

}
