package com.baiyi.cratos.service.session;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.SshSession;
import com.baiyi.cratos.domain.param.http.ssh.SshSessionParam;
import com.baiyi.cratos.mapper.SshSessionMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import lombok.NonNull;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/23 上午11:39
 * &#064;Version 1.0
 */
public interface SshSessionService extends BaseUniqueKeyService<SshSession, SshSessionMapper> {

    DataTable<SshSession> querySshSessionPage(SshSessionParam.SshSessionPageQuery pageQuery);

    SshSession getBySessionId(@NonNull String sessionId);

}
