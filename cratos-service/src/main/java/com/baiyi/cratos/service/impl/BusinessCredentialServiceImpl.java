package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessCredential;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.mapper.BusinessCredentialMapper;
import com.baiyi.cratos.service.BusinessCredentialService;
import com.baiyi.cratos.service.CredentialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/12 13:45
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.BUSINESS_CREDENTIAL)
public class BusinessCredentialServiceImpl implements BusinessCredentialService {

    private final BusinessCredentialMapper businessCredentialMapper;

    private final CredentialService credentialService;

    @Override
    public List<BusinessCredential> selectByBusiness(BaseBusiness.HasBusiness business) {
        Example example = new Example(BusinessCredential.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", business.getBusinessType())
                .andEqualTo("businessId", business.getBusinessId());
        return businessCredentialMapper.selectByExample(example);
    }

    public List<BusinessCredential> selectByKey(BaseBusiness.HasBusiness business) {
        Example example = new Example(BusinessCredential.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", business.getBusinessType())
                .andEqualTo("businessId", business.getBusinessId());
        return businessCredentialMapper.selectByExample(example);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void delete(BusinessCredential record) {
        // 一并删除私有凭据
        Credential credential = credentialService.getById(record.getCredentialId());
        if (credential != null && credential.getPrivateCredential()) {
            credentialService.deleteById(credential.getId());
        }
        deleteById(record.getId());
    }

    @Override
    public BusinessCredential getByUniqueKey(BusinessCredential record) {
        Example example = new Example(BusinessCredential.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", record.getBusinessType())
                .andEqualTo("businessId", record.getBusinessId())
                .andEqualTo("credentialId", record.getCredentialId());
        return getMapper().selectOneByExample(example);
    }

    @Override
    public List<BusinessCredential> queryByCredentialId(int credentialId) {
        Example example = new Example(BusinessCredential.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("credentialId", credentialId);
        return businessCredentialMapper.selectByExample(example);
    }

}
