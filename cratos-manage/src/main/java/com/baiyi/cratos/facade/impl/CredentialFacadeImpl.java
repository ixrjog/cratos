package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.common.exception.InvalidCredentialException;
import com.baiyi.cratos.common.exception.auth.AuthenticationException;
import com.baiyi.cratos.common.util.ExpiredUtil;
import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessCredential;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.credential.CredentialParam;
import com.baiyi.cratos.domain.view.credential.CredentialVO;
import com.baiyi.cratos.facade.CredentialFacade;
import com.baiyi.cratos.facade.validator.BaseFingerprintAlgorithm;
import com.baiyi.cratos.facade.validator.CredentialValidatorFactory;
import com.baiyi.cratos.facade.validator.ICredentialValidator;
import com.baiyi.cratos.service.BusinessCredentialService;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.wrapper.CredentialWrapper;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

import static com.baiyi.cratos.domain.ErrorEnum.NO_VALID_CREDENTIALS_AVAILABLE;

/**
 * @Author baiyi
 * @Date 2024/1/9 18:36
 * @Version 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class CredentialFacadeImpl implements CredentialFacade {

    private final CredentialService credentialService;

    private final BusinessCredentialService businessCredentialService;

    private final CredentialWrapper credentialWrapper;

    @Override
    public DataTable<CredentialVO.Credential> queryCredentialPage(CredentialParam.CredentialPageQuery pageQuery) {
        DataTable<Credential> table = credentialService.queryCredentialPage(pageQuery);
        return credentialWrapper.wrapToTarget(table);
    }

    @Override
    public void setCredentialValidById(int id) {
        credentialService.updateValidById(id);
    }

    // 查询用户有效的凭据

    @Override
    public void revokeCredentialById(int id) {
        Credential credential = Credential.builder()
                .id(id)
                .valid(false)
                .build();
        credentialService.updateByPrimaryKeySelective(credential);
    }

    @Override
    public List<Credential> queryCredentialByBusiness(BaseBusiness.IBusiness business) {
        List<BusinessCredential> businessCredentials = businessCredentialService.selectByBusiness(business);
        if (CollectionUtils.isEmpty(businessCredentials)) {
            return Collections.emptyList();
        }
        List<Credential> credentials = credentialService.queryByIds(businessCredentials.stream()
                .map(BusinessCredential::getCredentialId)
                .toList());
        return filterInvalidCredential(credentials);
    }

    private List<Credential> filterInvalidCredential(List<Credential> credentials) {
        List<Credential> result = Lists.newArrayList();
        credentials.stream()
                .filter(Credential::getValid)
                .forEachOrdered(credential -> {
                    if (ExpiredUtil.isExpired(credential.getExpiredTime())) {
                        credential.setValid(false);
                        credentialService.updateByPrimaryKey(credential);
                        return;
                    }
                    result.add(credential);
                });
        return result;
    }

    @Override
    public void createBusinessCredential(Credential credential, BaseBusiness.IBusiness business) {
        credentialService.add(credential);
        BusinessCredential businessCredential = BusinessCredential.builder()
                .businessType(business.getBusinessType())
                .businessId(business.getBusinessId())
                .credentialId(credential.getId())
                .build();
        businessCredentialService.add(businessCredential);
    }

    @Override
    public Credential getUserPasswordCredential(User user) {
        SimpleBusiness query = SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.USER.name())
                .businessId(user.getId())
                .build();
        List<BusinessCredential> businessCredentials = businessCredentialService.selectByBusiness(query);
        for (BusinessCredential businessCredential : businessCredentials) {
            Credential cred = credentialService.getById(businessCredential.getCredentialId());
            if (cred != null && cred.getPrivateCredential() && cred.getValid() && CredentialTypeEnum.USERNAME_WITH_PASSWORD.name()
                    .equals(cred.getCredentialType())) {
                // 判断过期
                if (ExpiredUtil.isExpired(cred.getExpiredTime())) {
                    cred.setValid(false);
                    credentialService.updateByPrimaryKey(cred);
                } else {
                    return cred;
                }
            }
        }
        throw new AuthenticationException(NO_VALID_CREDENTIALS_AVAILABLE);
    }

    @Override
    public void addCredential(CredentialParam.AddCredential addCredential) {
        Credential credential = addCredential.toTarget();
        CredentialTypeEnum credentialTypeEnum = CredentialTypeEnum.valueOf(credential.getCredentialType());
        ICredentialValidator credValidator = CredentialValidatorFactory.getValidator(credentialTypeEnum);
        if (credValidator == null) {
            throw new InvalidCredentialException("The credential type is incorrect.");
        }
        credValidator.verify(credential);
        if (credValidator instanceof BaseFingerprintAlgorithm baseFingerprintAlgorithm) {
            // 计算并填充指纹
            baseFingerprintAlgorithm.calcAndFillInFingerprint(credential);
        }
        credentialService.add(credential);
    }

    public void updateCredential(CredentialParam.AddCredential addCredential) {
        Credential credential = addCredential.toTarget();
        CredentialTypeEnum credentialTypeEnum = CredentialTypeEnum.valueOf(credential.getCredentialType());
        ICredentialValidator credValidator = CredentialValidatorFactory.getValidator(credentialTypeEnum);
        if (credValidator == null) {
            throw new InvalidCredentialException("The credential type is incorrect.");
        }
        credValidator.verify(credential);
        credentialService.add(credential);
    }

}
