package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessDecorator;
import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.AccountEntity;
import com.baiyi.cratos.domain.view.account.AccountEntityVO;
import com.baiyi.cratos.service.AccountEntityService;
import com.baiyi.cratos.wrapper.base.BaseBusinessDecorator;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/8 13:50
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.ACCOUNT_ENTITY)
public class AccountEntityWrapper extends BaseDataTableConverter<AccountEntityVO.AccountEntity, AccountEntity> implements BaseBusinessDecorator<AccountEntityVO.HasAccountEntity, AccountEntityVO.AccountEntity> {

    private final AccountEntityService accountEntityService;

    @Override
    @BusinessDecorator(types = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC})
    public void wrap(AccountEntityVO.AccountEntity vo) {
    }

    @Override
    public void decorateBusiness(AccountEntityVO.HasAccountEntity hasBusiness) {
        if (IdentityUtils.hasIdentity(hasBusiness.getAccountEntityId())) {
            AccountEntity accountEntity = accountEntityService.getById(hasBusiness.getAccountEntityId());
            if (accountEntity != null) {
                AccountEntityVO.AccountEntity vo = this.convert(accountEntity);
                delegateWrap(vo);
                hasBusiness.setAccountEntity(vo);
            }
        }
    }

}