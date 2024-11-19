package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.SshSessionInstanceCommand;
import com.baiyi.cratos.domain.param.ssh.SshCommandParam;
import com.baiyi.cratos.mapper.SshSessionInstanceCommandMapper;
import com.baiyi.cratos.service.SshSessionInstanceCommandService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/24 下午4:41
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
public class SshSessionInstanceCommandServiceImpl implements SshSessionInstanceCommandService {

    private final SshSessionInstanceCommandMapper sshSessionInstanceCommandMapper;

    @Override
    public DataTable<SshSessionInstanceCommand> querySshCommandPage(SshCommandParam.SshCommandPageQuery pageQuery) {
        Page<SshSessionInstanceCommand> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<SshSessionInstanceCommand> data = sshSessionInstanceCommandMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public int selectCountByInstanceId(int sshSessionInstanceId) {
        Example example = new Example(SshSessionInstanceCommand.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sshSessionInstanceId", sshSessionInstanceId);
        return sshSessionInstanceCommandMapper.selectCountByExample(example);
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:SSHSESSIONINSTANCECOMMAND:ID:' + #id")
    public void clearCacheById(int id) {
    }

}
