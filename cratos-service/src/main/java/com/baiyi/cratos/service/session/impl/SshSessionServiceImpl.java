package com.baiyi.cratos.service.session.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.SshSession;
import com.baiyi.cratos.domain.param.http.ssh.SshSessionParam;
import com.baiyi.cratos.mapper.SshSessionMapper;
import com.baiyi.cratos.service.session.SshSessionService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/23 上午11:40
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
public class SshSessionServiceImpl implements SshSessionService {

    private final SshSessionMapper sshSessionMapper;

    @Override
    public DataTable<SshSession> querySshSessionPage(SshSessionParam.SshSessionPageQuery pageQuery) {
        Page<SshSession> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<SshSession> data = sshSessionMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public SshSession getByUniqueKey(@NonNull SshSession record) {
        Example example = new Example(SshSession.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sessionId", record.getSessionId());
        return sshSessionMapper.selectOneByExample(example);
    }

    @Override
    public SshSession getBySessionId(@NonNull String sessionId) {
        SshSession uniqueKey = getByUniqueKey(SshSession.builder()
                .sessionId(sessionId)
                .build());
        return getByUniqueKey(uniqueKey);
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:SSHSESSION:ID:' + #id")
    public void clearCacheById(int id) {
    }

}
