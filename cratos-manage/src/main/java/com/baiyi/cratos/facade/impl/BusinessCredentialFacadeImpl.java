package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.generator.BusinessCredential;
import com.baiyi.cratos.facade.BusinessCredentialFacade;
import com.baiyi.cratos.service.BusinessCredentialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/2/6 14:11
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BusinessCredentialFacadeImpl implements BusinessCredentialFacade {

    private final BusinessCredentialService businessCredentialService;

    @Override
    public void revokeBusinessCredential(int id) {
        businessCredentialService.deleteById(id);
    }

    @Override
    public void revokeCredential(int credentialId) {
        List<BusinessCredential> businessCredentials = businessCredentialService.queryByCredentialId(credentialId);
        if (!CollectionUtils.isEmpty(businessCredentials)) {
            businessCredentials.forEach(businessCredentialService::delete);
        }
    }

    @Override
    public void revokeBusinessCredential(int credentialId, BaseBusiness.IBusiness business) {
        BusinessCredential query = BusinessCredential.builder()
                .credentialId(credentialId)
                .businessType(business.getBusinessType())
                .businessId(business.getBusinessId())
                .build();
        BusinessCredential businessCredential = businessCredentialService.getByUniqueKey(query);
        if (businessCredential != null) {
            businessCredentialService.deleteById(businessCredential.getId());
        }
    }

    @Override
    public void issueBusinessCredential(int credentialId, BaseBusiness.IBusiness business) {
        BusinessCredential businessCredential = BusinessCredential.builder()
                .credentialId(credentialId)
                .businessType(business.getBusinessType())
                .businessId(business.getBusinessId())
                .build();
        if (businessCredentialService.getByUniqueKey(businessCredential) == null) {
            businessCredentialService.add(businessCredential);
        }
    }

    @Override
    public void deleteBusinessCredentials(List<BusinessCredential> businessCredentialList) {
        if (!CollectionUtils.isEmpty(businessCredentialList)) {
            businessCredentialList.forEach(e -> businessCredentialService.deleteById(e.getId()));
        }
    }

    @Override
    public void updateBusinessCredential(Integer credentialId, BaseBusiness.IBusiness business) {
        List<BusinessCredential> businessCredentialList = businessCredentialService.selectByBusiness(business);
        if (IdentityUtil.hasIdentity(credentialId)) {
            if (businessCredentialList.stream()
                    .noneMatch(e -> credentialId.equals(e.getCredentialId()))) {
                deleteBusinessCredentials(businessCredentialList);
                BusinessCredential businessCredential = BusinessCredential.builder()
                        .businessId(business.getBusinessId())
                        .businessType(business.getBusinessType())
                        .credentialId(credentialId)
                        .build();
                businessCredentialService.add(businessCredential);
            }
        } else {
            // 删除
            deleteBusinessCredentials(businessCredentialList);
        }
    }

}
