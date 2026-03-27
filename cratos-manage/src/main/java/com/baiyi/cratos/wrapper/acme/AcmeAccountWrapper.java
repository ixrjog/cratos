package com.baiyi.cratos.wrapper.acme;

import com.baiyi.cratos.annotation.BusinessDecorator;
import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.AcmeAccount;
import com.baiyi.cratos.domain.view.acme.AcmeAccountVO;
import com.baiyi.cratos.service.acme.AcmeAccountService;
import com.baiyi.cratos.wrapper.base.BaseBusinessDecorator;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/27 16:03
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.ACME_ACCOUNT)
public class AcmeAccountWrapper extends BaseDataTableConverter<AcmeAccountVO.Account, AcmeAccount> implements BaseBusinessDecorator<AcmeAccountVO.HasAcmeAccount, AcmeAccountVO.Account> {

    private final AcmeAccountService acmeAccountService;

    @Override
    @BusinessDecorator(types = {BusinessTypeEnum.BUSINESS_TAG})
    public void wrap(AcmeAccountVO.Account vo) {
        // TODO
    }

    @Override
    public void decorateBusiness(AcmeAccountVO.HasAcmeAccount biz) {
        if (!IdentityUtils.hasIdentity(biz.getAccountId())) {
            return;
        }
        AcmeAccount acmeAccount = acmeAccountService.getById(biz.getAccountId());
        if (acmeAccount == null) {
            return;
        }
        AcmeAccountVO.Account accountVO = convert(acmeAccount);
        delegateWrap(accountVO);
        biz.setAccount(accountVO);
    }

}