package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.account.AccountEntityParam;
import com.baiyi.cratos.domain.view.account.AccountEntityVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/8 13:31
 * &#064;Version 1.0
 */
public interface AccountEntityFacade {

    DataTable<AccountEntityVO.AccountEntity> queryAccountEntityPage(
            AccountEntityParam.AccountEntityPageQuery pageQuery);

    void addAccountEntity(AccountEntityParam.AddAccountEntity addAccountEntity);

    void updateAccountEntity(AccountEntityParam.UpdateAccountEntity updateAccountEntity);

}
