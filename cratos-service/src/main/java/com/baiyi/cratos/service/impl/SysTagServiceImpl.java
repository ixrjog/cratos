package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.generator.SysTag;
import com.baiyi.cratos.mapper.SysTagMapper;
import com.baiyi.cratos.service.SysTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/2 13:28
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class SysTagServiceImpl implements SysTagService {

    private final SysTagMapper sysTagMapper;

    @Override
    public void add(SysTag sysTag) {
        sysTagMapper.insert(sysTag);
    }

    @Override
    public void updateByPrimaryKey(SysTag sysTag) {
        sysTagMapper.updateByPrimaryKey(sysTag);
    }

    @Override
    public void updateByPrimaryKeySelective(SysTag sysTag) {
        sysTagMapper.updateByPrimaryKeySelective(sysTag);
    }

    @Override
    public void deleteById(int id) {
        sysTagMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<SysTag> selectAll() {
       return sysTagMapper.selectAll();
    }

}