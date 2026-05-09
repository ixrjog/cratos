package com.baiyi.cratos.eds.security.apirisk.test.signature.impl;

import com.baiyi.cratos.eds.security.apirisk.test.enums.PrivateKeyType;
import com.baiyi.cratos.eds.security.apirisk.test.enums.SignatureAlgorithmEnum;
import com.baiyi.cratos.eds.security.apirisk.test.model.GenericCall;
import com.baiyi.cratos.eds.security.apirisk.test.signature.BaseSignatureAlgorithm;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/5/9 11:42
 * &#064;Version 1.0
 */
@Component
public class PASignatureAlgorithm extends BaseSignatureAlgorithm {

    private static final String[] SIGN_KEYS = {
            "token", "timestamp", "version", "device-id",
            "app-code", "device-type", "lang", "country-code", "param"
    };

    @Override
    public Map<String, String> calcSign(GenericCall.Request request, PrivateKeyType type) {
        Map<String, String> headers = request.getHeaders();
        String timestamp = String.valueOf(System.currentTimeMillis());
        headers.put("timestamp", timestamp);

        // 构建待签名参数
        Map<String, String> params = new TreeMap<>();
        for (String key : SIGN_KEYS) {
            if ("param".equals(key)) {
                params.put(key, filterValue(request.getBodyStr()));
            } else {
                params.put(key, filterValue(headers.get(key)));
            }
        }

        // 按key字典序拼接
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey().replace(" ", ""))
              .append(entry.getValue().replace(" ", ""));
        }

        String sign = hmacSha1Base64(sb.toString(), md5(timestamp));
        return Map.of("sign", sign, "timestamp", timestamp);
    }

    private String filterValue(String value) {
        return StringUtils.hasText(value) ? value : "";
    }

    private String hmacSha1Base64(String data, String key) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(secretKey);
            byte[] digest = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(digest);
        } catch (Exception e) {
            throw new RuntimeException("HmacSHA1 sign failed", e);
        }
    }

    private String md5(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(text.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("MD5 failed", e);
        }
    }

    @Override
    public SignatureAlgorithmEnum getSignatureAlgorithmEnum() {
        return SignatureAlgorithmEnum.PARTNERAPPSIGN;
    }

}
