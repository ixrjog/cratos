package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.generator.SshSessionInstance;
import com.baiyi.cratos.mapper.SshSessionInstanceMapper;
import com.baiyi.cratos.service.SshSessionInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/23 下午1:48
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
public class SshSessionInstanceServiceImpl implements SshSessionInstanceService {

    private final SshSessionInstanceMapper sshSessionInstanceMapper;

    @Override
    public SshSessionInstance getByUniqueKey(SshSessionInstance record) {
        Example example = new Example(SshSessionInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sessionId", record.getSessionId())
                .andEqualTo("instanceId", record.getInstanceId());
        return sshSessionInstanceMapper.selectOneByExample(example);
    }

}
