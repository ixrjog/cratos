package com.baiyi.cratos.facade.gitlab;

import com.baiyi.cratos.domain.param.http.event.GitLabEventParam;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/27 10:09
 * &#064;Version 1.0
 */
public interface GitLabFacade {

    void consumeEvent(GitLabEventParam.SystemHook systemHook, String hookToken);

}
