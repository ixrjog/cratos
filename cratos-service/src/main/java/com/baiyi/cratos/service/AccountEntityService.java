package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.AccountEntity;
import com.baiyi.cratos.domain.param.http.account.AccountEntityParam;
import com.baiyi.cratos.mapper.AccountEntityMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/8 13:25
 * &#064;Version 1.0
 */
public interface AccountEntityService extends BaseUniqueKeyService<AccountEntity, AccountEntityMapper> {

    DataTable<AccountEntity> queryAccountEntityPage(AccountEntityParam.AccountEntityPageQuery pageQuery);

}
