package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.generator.ServerAccount;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/20 下午4:19
 * &#064;Version 1.0
 */
public interface SimpleEdsAccountFacade {

    List<ServerAccount> queryServerAccounts();

}
