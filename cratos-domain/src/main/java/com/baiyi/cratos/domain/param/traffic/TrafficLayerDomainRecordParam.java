package com.baiyi.cratos.domain.param.traffic;

import com.baiyi.cratos.domain.generator.TrafficLayerDomainRecord;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Author baiyi
 * @Date 2024/3/29 17:04
 * @Version 1.0
 */
public class TrafficLayerDomainRecordParam {

    public interface IToTrafficLayerDomainRecord {

        Integer getDomainId();

        String getEnvName();

        default TrafficLayerDomainRecord toTrafficLayerDomainRecord() {
            return TrafficLayerDomainRecord.builder()
                    .domainId(getDomainId())
                    .envName(getEnvName())
                    .build();
        }

    }


    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class QueryRecordDetails extends PageParam implements IToTrafficLayerDomainRecord {

        @NotNull
        private Integer domainId;

        @NotBlank
        private String envName;

    }

}
