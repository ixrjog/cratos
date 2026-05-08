package com.baiyi.cratos.eds.security.apirisk.test.signature;

import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.eds.security.apirisk.test.enums.PrivateKeyType;
import com.baiyi.cratos.eds.security.apirisk.test.model.GenericCall;
import com.baiyi.cratos.service.CredentialService;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/5/7 13:49
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public abstract class BaseSignatureAlgorithm implements SignatureAlgorithm {

    private final CredentialService credentialService;

    /**
     * PKCS#8
     *
     * @param credentialId
     * @return
     */
    protected String getPrivateKey(int credentialId) {
        Credential credential = credentialService.getById(credentialId);
        return credential.getCredential();
    }

    @Override
    public Map<String, String> calcSign(GenericCall.Request request, PrivateKeyType type) {
        String privateKeyB64 = getPrivateKey(type);
        Map<String, String> headers = request.getHeaders();
        // 拼接基础字段
        String baseData = Joiner.on("")
                .join(
                        headers.getOrDefault("pp_device_id", ""), headers.getOrDefault("pp_device_type", ""),
                        headers.getOrDefault("pp_client_ver", ""), headers.getOrDefault("pp_timestamp", "")
                );
        String token = headers.getOrDefault("pp_token", "");
        String body = StringUtils.hasText(request.getBodyStr()) ? request.getBodyStr() : "";
        // pp_req_sign: 基础字段 + token(非空时拼接)
        String signData1 = baseData + (StringUtils.hasText(token) ? token : "");
        // pp_req_sign_2: 基础字段 + token(非空时拼接) + body(非空时拼接)
        String signData2 = signData1 + (StringUtils.hasText(body) ? body : "");
        return calcSign(request, privateKeyB64, signData1, signData2);
    }

    abstract protected Map<String, String> calcSign(GenericCall.Request request, String privateKeyB64, String signData1,
                                                    String signData2);

    abstract protected String getPrivateKey(PrivateKeyType type);

    protected String sign(String data, String privateKeyB64) {
        try {
            byte[] keyBytes = Base64.getDecoder()
                    .decode(privateKeyB64.replaceAll("\\s+", ""));
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            PrivateKey privateKey = KeyFactory.getInstance("RSA")
                    .generatePrivate(keySpec);
            // MD5withRSA 签名
            Signature signature = Signature.getInstance("MD5withRSA");
            signature.initSign(privateKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));
            byte[] signed = signature.sign();
            // Base64编码 + URL编码
            String base64Str = Base64.getEncoder()
                    .encodeToString(signed);
            return URLEncoder.encode(base64Str, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Sign failed", e);
        }
    }

}
