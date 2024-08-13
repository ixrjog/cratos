package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.generator.Server;
import com.baiyi.cratos.mapper.ServerMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.service.base.SupportBusinessService;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/12 上午10:36
 * &#064;Version 1.0
 */
public interface ServerService extends BaseUniqueKeyService<Server>, BaseValidService<Server, ServerMapper>, SupportBusinessService {
}
