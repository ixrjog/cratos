package com.baiyi.cratos.eds.security.apirisk.test.signature.impl;

import com.baiyi.cratos.eds.security.apirisk.test.enums.PrivateKeyType;
import com.baiyi.cratos.eds.security.apirisk.test.enums.SignatureAlgorithmEnum;
import com.baiyi.cratos.eds.security.apirisk.test.model.GenericCall;
import com.baiyi.cratos.eds.security.apirisk.test.signature.BaseSignatureAlgorithm;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/5/8 17:55
 * &#064;Version 1.0
 */
@Component
public class AdminPalmMerchantSignatureAlgorithm extends BaseSignatureAlgorithm {

    @Override
    public Map<String, String> calcSign(GenericCall.Request request, PrivateKeyType type) {
        Map<String, String> headers = request.getHeaders();
        String timestamp = String.valueOf(System.currentTimeMillis());
        headers.put("timestamp", timestamp);
        // GET请求用URL query参数（URL解码后），POST请求用body
        String param = getParam(request);
        String signData = "app-code" + filterValue(headers.get("app-code"))
                + "country-code" + filterValue(headers.get("country-code"))
                + "device-id" + filterValue(headers.get("device-id"))
                + "device-type" + filterValue(headers.get("device-type"))
                + "lang" + filterValue(headers.get("lang"))
                + "param" + filterValue(param)
                + "timestamp" + timestamp
                + "token" + filterValue(headers.get("token"))
                + "version" + filterValue(headers.get("version"));
        String sign = hmacSha1Base64(signData, md5(timestamp));
        return Map.of("sign", sign, "timestamp", timestamp);
    }

    private String getParam(GenericCall.Request request) {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            String url = request.getUrl();
            int paramIdx = url.indexOf("param=");
            if (paramIdx > 0) {
                String encoded = url.substring(paramIdx + 6);
                int ampIdx = encoded.indexOf('&');
                if (ampIdx > 0) {
                    encoded = encoded.substring(0, ampIdx);
                }
                try {
                    return URLDecoder.decode(encoded, StandardCharsets.UTF_8);
                } catch (Exception e) {
                    return encoded;
                }
            }
            return "";
        }
        return request.getBodyStr() != null ? request.getBodyStr() : "";
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
        return SignatureAlgorithmEnum.ADMINPALMMERCHANTSIGN;
    }

}
