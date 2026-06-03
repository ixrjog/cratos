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
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;

@Component
public class NileWebSignatureAlgorithm extends BaseSignatureAlgorithm {

    private static final String[] SIGN_KEYS = {
            "OP-M-TOKEN", "OP-BLACK-BOX", "timestamp", "version",
            "device-id", "app-code", "device-type", "lang", "countryCode"
    };

    @Override
    public Map<String, String> calcSign(GenericCall.Request request, PrivateKeyType type) {
        Map<String, String> headers = request.getHeaders();
        String timestamp = String.valueOf(System.currentTimeMillis());

        // 1. Prepare sign params from headers
        TreeMap<String, String> params = new TreeMap<>();
        for (String key : SIGN_KEYS) {
            params.put(key, getHeaderIgnoreCase(headers, key));
        }
        // Override timestamp with new value
        params.put("timestamp", timestamp);

        // param: empty for GET, body for POST
        String method = request.getMethod();
        if ("POST".equalsIgnoreCase(method) && StringUtils.hasText(request.getBodyStr())) {
            params.put("param", request.getBodyStr().replaceAll("\\s", ""));
        } else {
            params.put("param", "");
        }

        // 2. Sort by key (TreeMap already sorted) and join as key=value&key=value
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!first) {
                sb.append("&");
            }
            sb.append(entry.getKey()).append("=").append(entry.getValue());
            first = false;
        }
        String signStr = sb.toString();

        // 3. HmacSHA1 with key = MD5(timestamp), then Base64
        String sign = hmacSha1Base64(signStr, md5(timestamp));

        headers.put("timestamp", timestamp);
        headers.put("sign", sign);
        return Map.of("sign", sign, "timestamp", timestamp);
    }

    private String getHeaderIgnoreCase(Map<String, String> headers, String key) {
        String value = headers.get(key);
        if (value != null) return value;
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(key)) {
                return entry.getValue() != null ? entry.getValue() : "";
            }
        }
        return "";
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
        return SignatureAlgorithmEnum.NILEWEBSIGN;
    }

}
