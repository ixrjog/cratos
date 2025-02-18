package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.CommandExec;
import com.baiyi.cratos.domain.param.http.command.CommandExecParam;
import com.baiyi.cratos.mapper.CommandExecMapper;
import com.baiyi.cratos.service.CommandExecService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/13 16:04
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
public class CommandExecServiceImpl implements CommandExecService {

    private final CommandExecMapper commandExecMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:COMMANDEXEC:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public DataTable<CommandExec> queryCommandExecPage(CommandExecParam.CommandExecPageQuery pageQuery) {
        Page<CommandExec> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<CommandExec> data = commandExecMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

}
