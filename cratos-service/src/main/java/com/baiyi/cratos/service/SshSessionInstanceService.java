package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.generator.SshSessionInstance;
import com.baiyi.cratos.mapper.SshSessionInstanceMapper;
import com.baiyi.cratos.service.base.BaseService;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/23 下午1:47
 * &#064;Version 1.0
 */
public interface SshSessionInstanceService extends BaseUniqueKeyService<SshSessionInstance>, BaseService<SshSessionInstance, SshSessionInstanceMapper> {
}
