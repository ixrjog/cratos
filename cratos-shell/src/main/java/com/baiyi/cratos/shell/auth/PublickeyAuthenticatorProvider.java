package com.baiyi.cratos.shell.auth;

import org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator;

/**
 * @Author baiyi
 * @Date 2024/4/17 上午11:45
 * @Version 1.0
 */
@FunctionalInterface
public interface PublickeyAuthenticatorProvider extends PublickeyAuthenticator {
}