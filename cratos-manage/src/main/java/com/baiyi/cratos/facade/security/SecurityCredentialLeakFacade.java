package com.baiyi.cratos.facade.security;

import com.baiyi.cratos.domain.view.security.LeakedCredentialVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/5/21 14:46
 * &#064;Version 1.0
 */
public interface SecurityCredentialLeakFacade {

    LeakedCredentialVO.Credentials detectLeak(String credential);

}
