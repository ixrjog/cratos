package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.generator.SshSessionInstance;
import com.baiyi.cratos.mapper.SshSessionInstanceMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import lombok.NonNull;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/23 下午1:47
 * &#064;Version 1.0
 */
public interface SshSessionInstanceService extends BaseUniqueKeyService<SshSessionInstance, SshSessionInstanceMapper> {

    List<SshSessionInstance> queryBySessionId(String sessionId);

    SshSessionInstance getByInstanceId(@NonNull String instanceId);

}
