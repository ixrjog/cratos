package com.baiyi.cratos.service;

import com.baiyi.cratos.business.PermissionBusinessService;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.ServerAccount;
import com.baiyi.cratos.domain.param.http.server.ServerAccountParam;
import com.baiyi.cratos.mapper.ServerAccountMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.service.base.SupportBusinessService;

/**
 * @Author baiyi
 * @Date 2024/3/22 15:48
 * @Version 1.0
 */
public interface ServerAccountService extends BaseUniqueKeyService<ServerAccount, ServerAccountMapper>, BaseValidService<ServerAccount, ServerAccountMapper>, SupportBusinessService, PermissionBusinessService<ServerAccount> {

    DataTable<ServerAccount> queryServerAccountPage(ServerAccountParam.ServerAccountPageQuery pageQuery);

}
