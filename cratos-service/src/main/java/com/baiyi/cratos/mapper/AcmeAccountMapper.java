package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.AcmeAccount;
import com.baiyi.cratos.domain.param.http.acme.AcmeAccountParam;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface AcmeAccountMapper extends Mapper<AcmeAccount> {

    List<AcmeAccount> queryPageByParam(AcmeAccountParam.AccountPageQuery pageQuery);

}