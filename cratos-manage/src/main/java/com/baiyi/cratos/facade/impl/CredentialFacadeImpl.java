package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.common.exception.InvalidCredentialException;
import com.baiyi.cratos.common.exception.auth.AuthenticationException;
import com.baiyi.cratos.common.util.ExpiredUtils;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessCredential;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.credential.CredentialParam;
import com.baiyi.cratos.domain.view.credential.CredentialVO;
import com.baiyi.cratos.facade.CredentialFacade;
import com.baiyi.cratos.facade.validator.credential.BaseFingerprintAlgorithm;
import com.baiyi.cratos.facade.validator.credential.CredentialValidatorFactory;
import com.baiyi.cratos.facade.validator.credential.BaseCredentialValidator;
import com.baiyi.cratos.service.BusinessCredentialService;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.wrapper.CredentialWrapper;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
@RequiredArgsConstructor
public class CredentialFacadeImpl implements CredentialFacade {

    private final CredentialService credentialService;
    private final BusinessCredentialService businessCredentialService;
    private final CredentialWrapper credentialWrapper;

    @Override
    public DataTable<CredentialVO.Credential> queryCredentialPage(CredentialParam.CredentialPageQuery pageQuery) {
        DataTable<Credential> table = credentialService.queryCredentialPage(pageQuery);
        return credentialWrapper.wrapToTarget(table);
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
    public List<Credential> queryCredentialByBusiness(BaseBusiness.HasBusiness business) {
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
                    if (ExpiredUtils.isExpired(credential.getExpiredTime())) {
                        credential.setValid(false);
                        credentialService.updateByPrimaryKey(credential);
                        return;
                    }
                    result.add(credential);
                });
        return result;
    }

    @Override
    public void createBusinessCredential(Credential credential, BaseBusiness.HasBusiness business) {
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
                if (ExpiredUtils.isExpired(cred.getExpiredTime())) {
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
        BaseCredentialValidator credValidator = CredentialValidatorFactory.getValidator(credentialTypeEnum);
        if (credValidator == null) {
            InvalidCredentialException.runtime("The credential type is incorrect.");
        }
        credValidator.verify(credential);
        if (credValidator instanceof BaseFingerprintAlgorithm fingerprintAlgorithm) {
            // 计算并填充指纹
            fingerprintAlgorithm.calcAndFillInFingerprint(credential);
        }
        credentialService.add(credential);
    }

    @Override
    public void updateCredential(CredentialParam.UpdateCredential updateCredential) {
        Credential credential = Credential.builder()
                .id(updateCredential.getId())
                .title(StringUtils.hasText(updateCredential.getTitle()) ? updateCredential.getTitle() : "")
                .username(StringUtils.hasText(updateCredential.getUsername()) ? updateCredential.getUsername() : "")
                .comment(StringUtils.hasText(updateCredential.getComment()) ? updateCredential.getComment() : "")
                .build();
        Credential dbCredential = getById(updateCredential.getId());
        if (updateCredential.getValid()) {
            if (ExpiredUtils.isExpired(dbCredential.getExpiredTime())) {
                throw new InvalidCredentialException("The credential have expired.");
            }
            credential.setValid(updateCredential.getValid());
        }
        credentialService.updateByPrimaryKeySelective(credential);
    }

    @Override
    public void deleteById(int id) {
        // try
        getById(id);
        // 删除所有关联的业务凭据
        List<BusinessCredential> businessCredentials = businessCredentialService.queryByCredentialId(id);
        if (!CollectionUtils.isEmpty(businessCredentials)) {
            businessCredentials.forEach(businessCredentialService::delete);
        }
        // 删除凭据
        credentialService.deleteById(id);
    }

    @Override
    public void deleteMyCredentialById(int id) {
        Credential credential = getById(id);
        if (!credential.getPrivateCredential()) {
            throw new InvalidCredentialException("The credential is not private.");
        }
        String username = SessionUtils.getUsername();
        if (StringUtils.hasText(username) && username.equals(credential.getUsername())) {
            deleteById(id);
        } else {
            throw new InvalidCredentialException("The credential is not belong to you.");
        }
    }

    private Credential getById(int id) {
        Credential credential = credentialService.getById(id);
        if (credential == null) {
            throw new InvalidCredentialException("The credential do not exist.");
        }
        return credential;
    }

    @Override
    public BaseValidService<?, ?> getValidService() {
        return credentialService;
    }

}
