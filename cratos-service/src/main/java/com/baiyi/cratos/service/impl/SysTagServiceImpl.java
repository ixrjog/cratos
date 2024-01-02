package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.generator.SysTag;
import com.baiyi.cratos.mapper.SysTagMapper;
import com.baiyi.cratos.service.SysTagService;
import com.baiyi.cratos.service.base.AbstractService;
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
public class SysTagServiceImpl extends AbstractService<SysTag,SysTagMapper> {

    private final SysTagMapper sysTagMapper;

    @Override
    protected SysTagMapper getMapper() {
        return sysTagMapper;
    }

}