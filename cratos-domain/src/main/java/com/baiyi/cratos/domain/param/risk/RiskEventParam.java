package com.baiyi.cratos.domain.param.risk;

import com.baiyi.cratos.domain.generator.RiskEvent;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import com.baiyi.cratos.domain.param.tag.BusinessTagParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;


/**
 * @Author baiyi
 * @Date 2024/4/15 下午3:16
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class RiskEventParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class RiskEventPageQuery extends PageParam implements BusinessTagParam.HasQueryByTag {

        @Schema(description = "Query by name")
        private String queryName;

        private String year;

        private String quarter;

        private Integer weeks;

        private String states;

        private Boolean valid;

        private Boolean sla;

        private BusinessTagParam.QueryByTag queryByTag;

        private List<Integer> eventIdList;

        public RiskEventPageQueryParam toParam() {
            return RiskEventPageQueryParam.builder()
                    .queryName(queryName)
                    .year(year)
                    .quarter(quarter)
                    .weeks(weeks)
                    .states(states)
                    .valid(valid)
                    .sla(sla)
                    .page(getPage())
                    .length(getLength())
                    .eventIdList(eventIdList)
                    .build();
        }

        @Override
        public void setIdList(List<Integer> idList) {
            eventIdList = idList;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class RiskEventPageQueryParam extends PageParam {

        private String queryName;

        private String year;

        private String quarter;

        private Integer weeks;

        private String states;

        private Boolean valid;

        private Boolean sla;

        private List<Integer> eventIdList;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class RiskEventGraphQuery implements BusinessTagParam.HasQueryByTag {

        @NotBlank
        private String year;

        private String quarter;

        private BusinessTagParam.QueryByTag queryByTag;

        @Override
        public void setIdList(List<Integer> idList) {
            //
        }

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
