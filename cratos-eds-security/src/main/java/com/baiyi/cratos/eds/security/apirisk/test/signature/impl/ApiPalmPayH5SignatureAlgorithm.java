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
 * &#064;Date  2026/5/11 15:41
 * &#064;Version 1.0
 */
@Component
public class ApiPalmPayH5SignatureAlgorithm extends BaseSignatureAlgorithm {

    @Override
    public Map<String, String> calcSign(GenericCall.Request request, PrivateKeyType type) {
        Map<String, String> headers = request.getHeaders();
        String timestamp = String.valueOf(System.currentTimeMillis());
        headers.put("pp_timestamp", timestamp);

        // 构建签名参数
        Map<String, Object> params = new TreeMap<>();
        params.put("PP_TIMESTAMP", timestamp);
        params.put("PP_DEVICE_TYPE", filterValue(headers.get("pp_device_type")));
        params.put("countryCode", filterValue(headers.get("countrycode")));
        params.put("PP_DEVICE_ID", filterValue(headers.get("pp_device_id")));
        params.put("appSource", filterValue(headers.get("appsource")));
        params.put("PP_CLIENT_VER", filterValue(headers.get("pp_client_ver")));
        params.put("param", filterValue(request.getBodyStr()));

        // 按 key 字典序拼接 key+value
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            sb.append(entry.getKey()).append(entry.getValue().toString());
        }
        String signStr = sb.toString().replace(" ", "");

        String sign = hmacSha1Base64(signStr, md5(timestamp));
        return Map.of("pp_req_sign", sign, "pp_timestamp", timestamp);
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
        return SignatureAlgorithmEnum.APIPALMPAYH5SIGN;
    }

}
