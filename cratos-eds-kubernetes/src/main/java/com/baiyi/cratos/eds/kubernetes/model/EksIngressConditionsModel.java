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
 * &#064;Date  2024/6/27 下午5:40
 * &#064;Version 1.0
 */
public class EksIngressConditionsModel {

    /**
     * alb.ingress.kubernetes.io/conditions.source-ip-example |
     * [{"field":"source-ip","sourceIpConfig":{"values":["8.208.8.129/32", "172.16.0.0/16"]}}]
     */
    public interface HasField {

        String getField();

    }

    @Data
    public static class SourceIP implements HasField, BaseIngressConditionsModel.HasSourceIpConfig {

        private String field;

        private BaseIngressConditionsModel.SourceIpConfig sourceIpConfig;

    }

    private static List<SourceIP> loadAs(String annotation) {
        Type listType = new TypeToken<List<SourceIP>>() {
        }.getType();
        return new GsonBuilder().create()
                .fromJson(annotation, listType);
    }

    public static String getSourceIP(String annotation) {
        List<EksIngressConditionsModel.SourceIP> eksSourceIPs = EksIngressConditionsModel.loadAs(annotation);
        if (!CollectionUtils.isEmpty(eksSourceIPs)) {
            EksIngressConditionsModel.SourceIP eksSourceIP = eksSourceIPs.getFirst();
            if ("source-ip".equals(eksSourceIP.getField())) {
                return Joiner.on("|")
                        .skipNulls()
                        .join(eksSourceIP.getSourceIpConfig()
                                .getValues());
            }
        }
        return null;
    }

}
