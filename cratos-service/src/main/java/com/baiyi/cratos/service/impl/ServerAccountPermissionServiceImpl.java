package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.mapper.ServerAccountPermissionMapper;
import com.baiyi.cratos.service.ServerAccountPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author baiyi
 * @Date 2024/3/22 16:00
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class ServerAccountPermissionServiceImpl implements ServerAccountPermissionService {

    private final ServerAccountPermissionMapper serverAccountPermissionMapper;

}
