package com.baiyi.cratos.eds.security.apirisk.test.generic;

import com.baiyi.cratos.eds.security.apirisk.test.model.GenericCall;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpRequestParser {

    public static GenericCall.Request parse(String requestText) {
        return parse(requestText, true);
    }

    public static GenericCall.Request parse(String requestText, boolean filterHeaders) {
        GenericCall.Request parsed =  GenericCall.Request.builder().build();
        String[] lines = requestText.split("\n");
        if (lines.length == 0) {
            return parsed;
        }
        String requestLine = lines[0].trim();
        parseRequestLine(requestLine, parsed);
        Map<String, String> headers = new HashMap<>();
        StringBuilder bodyBuilder = new StringBuilder();
        boolean inBody = false;
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) {
                inBody = true;
                continue;
            }
            if (!inBody) {
                int colonIndex = line.indexOf(':');
                if (colonIndex > 0) {
                    String key = line.substring(0, colonIndex)
                            .trim();
                    String value = line.substring(colonIndex + 1)
                            .trim();
                    // 如果需要过滤header
                    if (filterHeaders) {
                        if (shouldFilterHeader(key)) {
                            continue;
                        }
                    }
                    headers.put(key, value);
                }
            } else {
                bodyBuilder.append(line);
            }
        }
        parsed.setHeaders(headers);
        String body = bodyBuilder.toString();
        if (!body.isEmpty()) {
            parsed.setBodyStr(body);
            parsed.setBody(parseJsonBody(body));
        }
        return parsed;
    }

    private static boolean shouldFilterHeader(String headerKey) {
        String key = headerKey.toLowerCase();
        // 1. 移除所有"cf-"前缀的header
        if (key.startsWith("cf-")) {
            return true;
        }
        // 2. 移除特定header
        return key.equals("remoteip") || key.equals("x-forwarded-proto") || key.equals("x-forwarded-for");
    }

    private static void parseRequestLine(String requestLine, GenericCall.Request parsed) {
        String[] parts = requestLine.split(" ");
        if (parts.length >= 2) {
            parsed.setMethod(parts[0]);
            String url = parts[1];
            if (url.startsWith("http://")) {
                url = "https://" + url.substring(7);
            }
            parsed.setUrl(url);
        }
    }

    private static Map<String, Object> parseJsonBody(String body) {
        try {
            body = body.trim();
            if (body.startsWith("{") && body.endsWith("}")) {
                Map<String, Object> result = new HashMap<>();
                String content = body.substring(1, body.length() - 1)
                        .trim();
                if (!content.isEmpty()) {
                    String[] pairs = content.split(",");
                    Arrays.stream(pairs)
                            .map(pair -> pair.split(":", 2))
                            .filter(kv -> kv.length == 2)
                            .forEach(kv -> {
                                String key = kv[0].trim()
                                        .replace("\"", "");
                                String value = kv[1].trim()
                                        .replace("\"", "");
                                result.put(key, value);
                            });
                }
                return result;
            }
        } catch (Exception ignored) {
        }
        return null;
    }

}