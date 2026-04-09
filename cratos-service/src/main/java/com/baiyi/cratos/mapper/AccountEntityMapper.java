package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.AccountEntity;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface AccountEntityMapper extends Mapper<AccountEntity> {
}