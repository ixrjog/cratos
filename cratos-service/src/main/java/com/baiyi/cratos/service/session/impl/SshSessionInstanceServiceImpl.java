package com.baiyi.cratos.service.session.impl;

import com.baiyi.cratos.domain.generator.SshSessionInstance;
import com.baiyi.cratos.mapper.SshSessionInstanceMapper;
import com.baiyi.cratos.service.session.SshSessionInstanceService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

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
    public SshSessionInstance getByUniqueKey(@NonNull SshSessionInstance record) {
        Example example = new Example(SshSessionInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", record.getInstanceId());
        return sshSessionInstanceMapper.selectOneByExample(example);
    }

    @Override
    public SshSessionInstance getByInstanceId(@NonNull String instanceId) {
        SshSessionInstance uniqueKey = SshSessionInstance.builder()
                .instanceId(instanceId)
                .build();
        return this.getByUniqueKey(uniqueKey);
    }

    @Override
    public List<SshSessionInstance> queryBySessionId(String sessionId) {
        Example example = new Example(SshSessionInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sessionId", sessionId);
        return sshSessionInstanceMapper.selectByExample(example);
    }

    @Override
    public int countBySessionId(@NonNull String sessionId) {
        Example example = new Example(SshSessionInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sessionId", sessionId);
        return sshSessionInstanceMapper.selectCountByExample(example);
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:SSHSESSIONINSTANCE:ID:' + #id")
    public void clearCacheById(int id) {
    }

}
