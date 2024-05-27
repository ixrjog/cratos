package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.SshSessionInstanceCommand;
import com.baiyi.cratos.domain.param.ssh.SshCommandParam;
import com.baiyi.cratos.mapper.SshSessionInstanceCommandMapper;
import com.baiyi.cratos.service.SshSessionInstanceCommandService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

}
