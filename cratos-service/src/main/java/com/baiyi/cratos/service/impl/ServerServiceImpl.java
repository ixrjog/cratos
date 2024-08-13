package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Server;
import com.baiyi.cratos.service.ServerService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/12 上午10:36
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.SERVER)
public class ServerServiceImpl implements ServerService {

    @Override
    public Server getByUniqueKey(@NonNull Server record) {
        Example example = new Example(Server.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("privateIp", record.getPrivateIp());
        return getMapper().selectOneByExample(example);
    }

}
