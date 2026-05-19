package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.generator.UserCredentialWebauthn;

import java.util.List;
import java.util.Map;

public interface WebAuthnFacade {

    Map<String, Object> getRegistrationOptions();

    void completeRegistration(Map<String, Object> credential);

    Map<String, Object> getLoginOptions(String username);

    Map<String, Object> completeLogin(Map<String, Object> assertion);

    List<UserCredentialWebauthn> listMyCredentials();

    void deleteCredential(int id);

}
