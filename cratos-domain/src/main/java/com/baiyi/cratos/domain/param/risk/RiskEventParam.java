package com.baiyi.cratos.domain.param.risk;

import com.baiyi.cratos.domain.generator.RiskEvent;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.Set;

/**
 * @Author baiyi
 * @Date 2024/4/15 下午3:16
 * @Version 1.0
 */
public class RiskEventParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class RiskEventPageQuery extends PageParam {

        @Schema(description = "Query by name")
        private String queryName;

        private String year;

        private String quarter;

        private Integer weeks;

        private String states;

        private Boolean valid;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class RiskEventReportQuery  {

        private String year;

        private String quarter;

        private Integer weeks;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class RiskEventReportQueryByTags  {

        private String year;

        private String quarter;

        private Integer weeks;

        private Set<Integer> byTags;

    }

    @Data
    @Schema
    public static class AddRiskEvent implements IToTarget<RiskEvent> {

        private Integer id;

        private String name;

        @NotNull
        private Date eventTime;

        private String states;

        private String year;

        private String quarter;

        private Integer weeks;

        private String color;

        private Boolean valid;

        private String comment;

    }

    @Data
    @Schema
    public static class UpdateRiskEvent implements IToTarget<RiskEvent> {

        private Integer id;

        private String name;

        @NotNull
        private Date eventTime;

        private String states;

        private String year;

        private String quarter;

        private Integer weeks;

        private String color;

        private Boolean valid;

        private String comment;

    }

}
