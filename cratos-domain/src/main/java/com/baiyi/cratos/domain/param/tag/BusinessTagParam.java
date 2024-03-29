package com.baiyi.cratos.domain.param.tag;

import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.domain.param.IToTarget;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2024/1/5 17:54
 * @Version 1.0
 */
public class BusinessTagParam {

    @Data
    @Schema
    public static class QueryByValue {

        @NotNull
        private Integer tagId;

        private String queryTagValue;

    }

    @Data
    @Schema
    public static class SaveBusinessTag implements IToTarget<BusinessTag> {

        private Integer id;

        private String businessType;

        private Integer businessId;

        private Integer tagId;

        private String tagValue;

    }

    @Data
    @Schema
    public static class AddBusinessTag implements IToTarget<BusinessTag> {

        private String businessType;

        private Integer businessId;

        private Integer tagId;

        private String tagValue;

    }

    @Data
    @Schema
    public static class UpdateBusinessTag implements IToTarget<BusinessTag> {

        private Integer id;

        private String businessType;

        private Integer businessId;

        private Integer tagId;

        private String tagValue;

    }

}
