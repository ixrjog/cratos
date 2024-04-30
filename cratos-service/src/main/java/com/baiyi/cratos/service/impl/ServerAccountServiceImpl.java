package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.ServerAccount;
import com.baiyi.cratos.mapper.ServerAccountMapper;
import com.baiyi.cratos.service.ServerAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * @Author baiyi
 * @Date 2024/3/22 15:48
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.SERVER_ACCOUNT)
public class ServerAccountServiceImpl implements ServerAccountService {

    private final ServerAccountMapper serverAccountMapper;

    @Override
    public ServerAccount getByUniqueKey(ServerAccount record) {
        Example example = new Example(ServerAccount.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", record.getName());
        return serverAccountMapper.selectOneByExample(example);
    }

}
