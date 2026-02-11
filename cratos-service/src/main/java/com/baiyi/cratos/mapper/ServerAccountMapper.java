package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.ServerAccount;
import com.baiyi.cratos.domain.param.http.server.ServerAccountParam;
import com.baiyi.cratos.query.ServerAccountQuery;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface ServerAccountMapper extends Mapper<ServerAccount> {

    List<ServerAccount> queryPageByParam(ServerAccountParam.ServerAccountPageQuery pageQuery);

    List<ServerAccount> queryUserPermissionServerAccounts(
            ServerAccountQuery.QueryUserPermissionServerAccountParam param);

}