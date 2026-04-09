package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.AccountEntity;
import com.baiyi.cratos.domain.param.http.account.AccountEntityParam;
import com.baiyi.cratos.domain.view.account.AccountEntityVO;
import com.baiyi.cratos.facade.AccountEntityFacade;
import com.baiyi.cratos.service.AccountEntityService;
import com.baiyi.cratos.wrapper.AccountEntityWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/8 13:31
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AccountEntityFacadeImpl implements AccountEntityFacade {

    private final AccountEntityService accountEntityService;
    private final AccountEntityWrapper accountEntityWrapper;

    @Override
    public DataTable<AccountEntityVO.AccountEntity> queryAccountEntityPage(AccountEntityParam.AccountEntityPageQuery pageQuery) {
        DataTable<AccountEntity> table = accountEntityService.queryAccountEntityPage(pageQuery);
        return accountEntityWrapper.wrapToTarget(table);
    }

    @Override
    public void addAccountEntity(AccountEntityParam.AddAccountEntity addAccountEntity) {
        AccountEntity entity = addAccountEntity.toTarget();
        entity.setValid(true);
        accountEntityService.add(entity);
    }

    @Override
    public void updateAccountEntity(AccountEntityParam.UpdateAccountEntity updateAccountEntity) {
        accountEntityService.updateByPrimaryKey(updateAccountEntity.toTarget());
    }

}
