package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.RedisUtil;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.generator.UserCredentialWebauthn;
import com.baiyi.cratos.domain.generator.UserToken;
import com.baiyi.cratos.facade.UserTokenFacade;
import com.baiyi.cratos.facade.WebAuthnFacade;
import com.baiyi.cratos.service.UserCredentialWebauthnService;
import com.baiyi.cratos.service.UserService;
import com.webauthn4j.WebAuthnManager;
import com.webauthn4j.authenticator.AuthenticatorImpl;
import com.webauthn4j.converter.AttestedCredentialDataConverter;
import com.webauthn4j.converter.util.ObjectConverter;
import com.webauthn4j.data.*;
import com.webauthn4j.data.attestation.authenticator.AttestedCredentialData;
import com.webauthn4j.data.client.Origin;
import com.webauthn4j.data.client.challenge.Challenge;
import com.webauthn4j.data.client.challenge.DefaultChallenge;
import com.webauthn4j.server.ServerProperty;
import com.webauthn4j.verifier.exception.VerificationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebAuthnFacadeImpl implements WebAuthnFacade {

    private final UserCredentialWebauthnService webauthnService;
    private final UserTokenFacade userTokenFacade;
    private final RedisUtil redisUtil;
    private final UserService userService;

    @Value("${webauthn.rp-id:cratos.palmpay-inc.com}")
    private String rpId;

    @Value("${webauthn.rp-name:Cratos}")
    private String rpName;

    @Value("${webauthn.origin:https://cratos.palmpay-inc.com}")
    private String origin;

    private static final long CHALLENGE_TTL = 300; // 5 minutes
    private static final String CHALLENGE_KEY_PREFIX = "WEBAUTHN:CHALLENGE:";

    @Override
    public Map<String, Object> getRegistrationOptions() {
        String username = getCurrentUsername();
        byte[] challenge = generateChallenge();
        // Store challenge in Redis
        redisUtil.set(
                CHALLENGE_KEY_PREFIX + "REG:" + username, Base64.getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(challenge), CHALLENGE_TTL
        );

        byte[] userId = username.getBytes();
        List<UserCredentialWebauthn> existing = webauthnService.queryByUsername(username);
        List<Map<String, Object>> excludeCredentials = existing.stream()
                .map(c -> Map.<String, Object>of("type", "public-key", "id", c.getCredentialId()))
                .toList();

        Map<String, Object> options = new LinkedHashMap<>();
        options.put(
                "challenge", Base64.getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(challenge)
        );
        options.put("rp", Map.of("id", rpId, "name", rpName));
        options.put(
                "user", Map.of(
                        "id", Base64.getUrlEncoder()
                                .withoutPadding()
                                .encodeToString(userId), "name", username, "displayName", username
                )
        );
        options.put(
                "pubKeyCredParams", List.of(
                        Map.of("type", "public-key", "alg", -7),   // ES256
                        Map.of("type", "public-key", "alg", -257)  // RS256
                )
        );
        options.put(
                "authenticatorSelection",
                Map.of("authenticatorAttachment", "platform", "userVerification", "required",
                        "residentKey", "required", "requireResidentKey", true)
        );
        options.put("timeout", 60000);
        options.put("attestation", "none");
        options.put("excludeCredentials", excludeCredentials);
        return options;
    }

    @Override
    public void completeRegistration(Map<String, Object> credential) {
        String username = getCurrentUsername();
        String challengeStr = (String) redisUtil.get(CHALLENGE_KEY_PREFIX + "REG:" + username);
        if (challengeStr == null) {
            throw new RuntimeException("Challenge expired or not found");
        }

        String credentialId = (String) credential.get("id");
        Map<String, Object> response = (Map<String, Object>) credential.get("response");
        String attestationObject = (String) response.get("attestationObject");
        String clientDataJSON = (String) response.get("clientDataJSON");

        // Verify using webauthn4j
        WebAuthnManager webAuthnManager = WebAuthnManager.createNonStrictWebAuthnManager();
        byte[] attestationObjectBytes = Base64.getUrlDecoder()
                .decode(attestationObject);
        byte[] clientDataJSONBytes = Base64.getUrlDecoder()
                .decode(clientDataJSON);
        byte[] challengeBytes = Base64.getUrlDecoder()
                .decode(challengeStr);

        Origin originObj = new Origin(origin);
        Challenge challenge = new DefaultChallenge(challengeBytes);
        ServerProperty serverProperty = new ServerProperty(originObj, rpId, challenge, null);

        RegistrationRequest registrationRequest = new RegistrationRequest(attestationObjectBytes, clientDataJSONBytes);
        RegistrationParameters registrationParameters = new RegistrationParameters(serverProperty, null, false, true);

        RegistrationData registrationData;
        try {
            registrationData = webAuthnManager.parse(registrationRequest);
            webAuthnManager.validate(registrationData, registrationParameters);
        } catch (VerificationException e) {
            throw new RuntimeException("WebAuthn registration validation failed: " + e.getMessage());
        }

        AttestedCredentialData attestedCredentialData = registrationData.getAttestationObject()
                .getAuthenticatorData()
                .getAttestedCredentialData();

        // Serialize public key
        ObjectConverter objectConverter = new ObjectConverter();
        AttestedCredentialDataConverter converter = new AttestedCredentialDataConverter(objectConverter);
        String publicKeyBase64 = Base64.getEncoder()
                .encodeToString(converter.convert(attestedCredentialData));

        String aaguid = attestedCredentialData.getAaguid()
                .toString();
        long signCount = registrationData.getAttestationObject()
                .getAuthenticatorData()
                .getSignCount();

        // Get transports
        String transports = credential.containsKey("transports") ? String.join(
                ",", (List<String>) credential.get("transports")) : "internal";

        String deviceName = (String) credential.getOrDefault("deviceName", "Biometric Device");

        UserCredentialWebauthn record = UserCredentialWebauthn.builder()
                .username(username)
                .credentialId(credentialId)
                .publicKey(publicKeyBase64)
                .signCount(signCount)
                .aaguid(aaguid)
                .transports(transports)
                .deviceName(deviceName)
                .valid(true)
                .build();
        webauthnService.add(record);

        // Clean up challenge
        redisUtil.del(CHALLENGE_KEY_PREFIX + "REG:" + username);
    }

    @Override
    public Map<String, Object> getLoginOptions(String username) {
        List<UserCredentialWebauthn> credentials = webauthnService.queryByUsername(username);
        if (credentials.isEmpty()) {
            throw new RuntimeException("No WebAuthn credentials found for user: " + username);
        }

        byte[] challenge = generateChallenge();
        redisUtil.set(
                CHALLENGE_KEY_PREFIX + "LOGIN:" + username, Base64.getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(challenge), CHALLENGE_TTL
        );

        List<Map<String, Object>> allowCredentials = credentials.stream()
                .map(c -> {
                    Map<String, Object> cred = new LinkedHashMap<>();
                    cred.put("type", "public-key");
                    cred.put("id", c.getCredentialId());
                    if (c.getTransports() != null) {
                        cred.put(
                                "transports", Arrays.asList(c.getTransports()
                                                                    .split(","))
                        );
                    }
                    return cred;
                })
                .toList();

        Map<String, Object> options = new LinkedHashMap<>();
        options.put(
                "challenge", Base64.getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(challenge)
        );
        options.put("rpId", rpId);
        options.put("allowCredentials", allowCredentials);
        options.put("userVerification", "required");
        options.put("timeout", 60000);
        return options;
    }

    @Override
    public Map<String, Object> getLoginOptions() {
        byte[] challenge = generateChallenge();
        String challengeStr = Base64.getUrlEncoder().withoutPadding().encodeToString(challenge);
        redisUtil.set(CHALLENGE_KEY_PREFIX + "LOGIN:DISCOVERABLE:" + challengeStr, challengeStr, CHALLENGE_TTL);

        Map<String, Object> options = new LinkedHashMap<>();
        options.put("challenge", challengeStr);
        options.put("rpId", rpId);
        options.put("allowCredentials", List.of());
        options.put("userVerification", "required");
        options.put("timeout", 60000);
        return options;
    }

    @Override
    public Map<String, Object> completeLogin(Map<String, Object> assertion) {
        String credentialId = (String) assertion.get("id");
        String username = (String) assertion.get("username");

        // 无用户名模式：从 userHandle 或 credentialId 反查用户
        String challengeStr;
        if (username == null || username.isBlank()) {
            UserCredentialWebauthn storedCredential = webauthnService.getByCredentialId(credentialId);
            if (storedCredential == null || !storedCredential.getValid()) {
                throw new RuntimeException("Credential not found or invalid");
            }
            username = storedCredential.getUsername();

            // 尝试从 userHandle 获取用户名（优先）
            Map<String, Object> response = (Map<String, Object>) assertion.get("response");
            String userHandle = (String) response.get("userHandle");
            if (userHandle != null && !userHandle.isBlank()) {
                String decoded = new String(Base64.getUrlDecoder().decode(userHandle));
                if (!decoded.equals(username)) {
                    throw new RuntimeException("UserHandle does not match credential owner");
                }
            }

            // discoverable 模式的 challenge 验证
            challengeStr = findDiscoverableChallenge(response);
        } else {
            challengeStr = (String) redisUtil.get(CHALLENGE_KEY_PREFIX + "LOGIN:" + username);
        }

        if (challengeStr == null) {
            throw new RuntimeException("Challenge expired or not found");
        }

        UserCredentialWebauthn storedCredential = webauthnService.getByCredentialId(credentialId);
        if (storedCredential == null || !storedCredential.getValid()) {
            throw new RuntimeException("Credential not found or invalid");
        }
        if (!storedCredential.getUsername().equals(username)) {
            throw new RuntimeException("Credential does not belong to user");
        }

        Map<String, Object> response = (Map<String, Object>) assertion.get("response");
        String authenticatorData = (String) response.get("authenticatorData");
        String clientDataJSON = (String) response.get("clientDataJSON");
        String signature = (String) response.get("signature");

        // Verify using webauthn4j
        WebAuthnManager webAuthnManager = WebAuthnManager.createNonStrictWebAuthnManager();
        byte[] authenticatorDataBytes = Base64.getUrlDecoder().decode(authenticatorData);
        byte[] clientDataJSONBytes = Base64.getUrlDecoder().decode(clientDataJSON);
        byte[] signatureBytes = Base64.getUrlDecoder().decode(signature);
        byte[] challengeBytes = Base64.getUrlDecoder().decode(challengeStr);

        Origin originObj = new Origin(origin);
        Challenge challenge = new DefaultChallenge(challengeBytes);
        ServerProperty serverProperty = new ServerProperty(originObj, rpId, challenge, null);

        // Reconstruct authenticator
        ObjectConverter objectConverter = new ObjectConverter();
        AttestedCredentialDataConverter converter = new AttestedCredentialDataConverter(objectConverter);
        AttestedCredentialData attestedCredentialData = converter.convert(Base64.getDecoder()
                                                                                  .decode(storedCredential.getPublicKey()));
        AuthenticatorImpl authenticator = new AuthenticatorImpl(
                attestedCredentialData, null, storedCredential.getSignCount());

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(
                Base64.getUrlDecoder().decode(credentialId), authenticatorDataBytes, clientDataJSONBytes, signatureBytes
        );
        AuthenticationParameters authenticationParameters = new AuthenticationParameters(
                serverProperty, authenticator, null, false, true);

        AuthenticationData authenticationData;
        try {
            authenticationData = webAuthnManager.parse(authenticationRequest);
            webAuthnManager.validate(authenticationData, authenticationParameters);
        } catch (VerificationException e) {
            throw new RuntimeException("WebAuthn authentication failed: " + e.getMessage());
        }

        // Update sign count
        storedCredential.setSignCount(authenticationData.getAuthenticatorData().getSignCount());
        webauthnService.updateByPrimaryKey(storedCredential);

        // Issue token
        UserToken userToken = userTokenFacade.revokeAndIssueNewToken(username, "BIOMETRIC");

        // Clean up
        redisUtil.del(CHALLENGE_KEY_PREFIX + "LOGIN:" + username);

        User user = userService.getByUsername(username);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("token", userToken.getToken());
        result.put("jti", userToken.getJti());
        result.put("username", username);
        result.put("name", user.getName());
        return result;
    }

    private String findDiscoverableChallenge(Map<String, Object> response) {
        String clientDataJSON = (String) response.get("clientDataJSON");
        byte[] clientDataBytes = Base64.getUrlDecoder().decode(clientDataJSON);
        String clientDataStr = new String(clientDataBytes);
        // 从 clientDataJSON 中提取 challenge
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Map<String, Object> clientData = mapper.readValue(clientDataStr, Map.class);
            String challenge = (String) clientData.get("challenge");
            String redisKey = CHALLENGE_KEY_PREFIX + "LOGIN:DISCOVERABLE:" + challenge;
            String stored = (String) redisUtil.get(redisKey);
            if (stored != null) {
                redisUtil.del(redisKey);
                return challenge;
            }
        } catch (Exception e) {
            log.error("Failed to parse clientDataJSON for discoverable challenge", e);
        }
        return null;
    }

    @Override
    public List<UserCredentialWebauthn> listMyCredentials() {
        return webauthnService.queryByUsername(getCurrentUsername());
    }

    @Override
    public void deleteCredential(int id) {
        UserCredentialWebauthn credential = webauthnService.getById(id);
        if (credential != null && credential.getUsername()
                .equals(getCurrentUsername())) {
            webauthnService.deleteById(id);
        }
    }

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

    private byte[] generateChallenge() {
        byte[] challenge = new byte[32];
        new SecureRandom().nextBytes(challenge);
        return challenge;
    }

}
