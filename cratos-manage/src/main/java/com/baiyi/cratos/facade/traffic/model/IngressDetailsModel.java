package com.baiyi.cratos.facade.traffic.model;

import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.google.api.client.util.Lists;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Collections;
import java.util.List;

import static com.baiyi.cratos.facade.traffic.TrafficLayerIngressFacadeImpl.INGRESS_TABLE_FIELD_NAME;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/16 上午10:42
 * &#064;Version 1.0
 */
public class IngressDetailsModel {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IngressEntry implements Comparable<IngressEntry> {
        private String kubernetes;
        private String ingress;
        private String rule;
        private String service;
        private String lb;
        @Override
        public int compareTo(@NonNull IngressEntry o) {
            return service.compareTo(o.getService());
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IngressEntries {
        private List<IngressEntry> entries = Lists.newArrayList();
        public void add(IngressEntry ingressEntry) {
            entries.add(ingressEntry);
        }
        @Override
        public String toString() {
            PrettyTable ingressTable = PrettyTable.fieldNames(INGRESS_TABLE_FIELD_NAME);
            Collections.sort(entries);
            entries.forEach(ingressEntry -> ingressTable.addRow(ingressEntry.getKubernetes(), ingressEntry.getIngress(),
                    ingressEntry.getRule(), ingressEntry.getService(), ingressEntry.getLb()));
            return ingressTable.toString();
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IngressIndexDetails {
        private EdsAssetIndex namespace;
        @Schema(description = "loadBalancer.ingress.hostname")
        private EdsAssetIndex hostname;
        @Builder.Default
        private List<EdsAssetIndex> rules = Lists.newArrayList();
    }

}
