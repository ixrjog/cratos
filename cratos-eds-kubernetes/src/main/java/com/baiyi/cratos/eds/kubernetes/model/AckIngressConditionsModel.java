package com.baiyi.cratos.eds.kubernetes.model;

import com.google.common.base.Joiner;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Type;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/27 下午5:20
 * &#064;Version 1.0
 */
@Slf4j
public class AckIngressConditionsModel {

    /**
     * alb.ingress.kubernetes.io/conditions.source-ip-example: |
     * [{
     * "type": "SourceIp",
     * "sourceIpConfig": {
     * "values": [
     * "192.168.0.0/16",
     * "172.16.0.0/16"
     * ]
     * }
     * }]
     */
    public interface HasType {
        String getType();
    }

    @Data
    public static class SourceIP implements HasType, BaseIngressConditionsModel.HasSourceIpConfig {
        private String type;
        private BaseIngressConditionsModel.SourceIpConfig sourceIpConfig;
    }

    private static List<SourceIP> loadAs(String annotation) {
        try {
            Type listType = new TypeToken<List<SourceIP>>() {
            }.getType();
            return new GsonBuilder().create()
                    .fromJson(annotation, listType);
        } catch (Exception e) {
            log.error("Load sourceIPs for annotation {} err: {}", annotation, e.getMessage());
            return List.of();
        }
    }

    public static String getSourceIP(String annotation) {
        if (!StringUtils.hasText(annotation)) {
            return null;
        }
        List<AckIngressConditionsModel.SourceIP> ackSourceIPs = AckIngressConditionsModel.loadAs(annotation);
        if (!CollectionUtils.isEmpty(ackSourceIPs)) {
            AckIngressConditionsModel.SourceIP ackSourceIP = ackSourceIPs.getFirst();
            if ("SourceIp".equals(ackSourceIP.getType())) {
                return Joiner.on("|")
                        .skipNulls()
                        .join(ackSourceIP.getSourceIpConfig()
                                .getValues());
            }
        }
        return null;
    }

}
