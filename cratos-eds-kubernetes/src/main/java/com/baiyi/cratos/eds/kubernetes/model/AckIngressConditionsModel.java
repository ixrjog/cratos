package com.baiyi.cratos.eds.kubernetes.model;

import com.google.common.base.Joiner;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/27 下午5:20
 * &#064;Version 1.0
 */
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
        Type listType = new TypeToken<List<SourceIP>>() {
        }.getType();
        return new GsonBuilder().create()
                .fromJson(annotation, listType);
    }

    public static String getSourceIP(String annotation) {
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
