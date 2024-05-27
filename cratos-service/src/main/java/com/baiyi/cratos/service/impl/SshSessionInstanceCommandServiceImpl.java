package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.mapper.SshSessionInstanceCommandMapper;
import com.baiyi.cratos.service.SshSessionInstanceCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/24 下午4:41
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
public class SshSessionInstanceCommandServiceImpl implements SshSessionInstanceCommandService {

    private final SshSessionInstanceCommandMapper sshSessionInstanceCommandMapper;

}
