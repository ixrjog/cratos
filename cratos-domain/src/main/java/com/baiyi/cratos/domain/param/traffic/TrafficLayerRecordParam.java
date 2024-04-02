package com.baiyi.cratos.domain.param.traffic;

import com.baiyi.cratos.domain.generator.TrafficLayerDomainRecord;
import com.baiyi.cratos.domain.param.IToTarget;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2024/3/29 17:04
 * @Version 1.0
 */
public class TrafficLayerRecordParam {

    public interface IToTrafficLayerRecord {

        Integer getDomainId();

        String getEnvName();

        default TrafficLayerDomainRecord toTrafficLayerRecord() {
            return TrafficLayerDomainRecord.builder()
                    .domainId(getDomainId())
                    .envName(getEnvName())
                    .build();
        }

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class QueryRecordDetails implements IToTrafficLayerRecord {

        @NotNull
        private Integer domainId;

        @NotBlank
        private String envName;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class AddRecord implements IToTarget<TrafficLayerDomainRecord> {

        private Integer domainId;

        private String envName;

        private String recordName;

        private String routeTrafficTo;

        private String originServer;

        private Boolean valid;

        private String comment;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class UpdateRecord implements IToTarget<TrafficLayerDomainRecord> {

        private Integer id;

        private Integer domainId;

        private String envName;

        private String recordName;

        private String routeTrafficTo;

        private String originServer;

        private Boolean valid;

        private String comment;

    }

}
