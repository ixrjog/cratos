package com.baiyi.cratos.domain.param.http.tag;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/1/5 17:54
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class BusinessTagParam {

    public interface HasQueryByTag {
        BusinessTagParam.QueryByTag getQueryByTag();

        void setIdList(List<Integer> idList);

        default boolean isQueryByTag() {
            int tagId = Optional.ofNullable(getQueryByTag())
                    .map(BusinessTagParam.QueryByTag::getTagId)
                    .orElse(0);
            return tagId != 0;
        }
    }

    @Data
    @Builder
    @Schema
    public static class QueryByTag {
        private Integer tagId;
        private String businessType;
        private String queryTagValue;
        private String tagValue;
    }

    @Data
    @Builder
    @Schema
    public static class QueryBusinessTagValues {
        private Integer tagId;
        private String businessType;
        private List<Integer> businessIds;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
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

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class BusinessTagPageQuery extends PageParam {
        private String queryName;
        private Integer tagGroupId;
        private String businessType;
    }

    @Data
    @Schema
    public static class CopyBusinessTag implements BaseBusiness.HasBusiness {
        @NotBlank
        private String businessType;
        @NotNull
        private Integer businessId;
        @NotEmpty
        private List<CopyTo> copyTo;
        private Boolean covered;
    }

    @Data
    @Schema
    public static class CopyTo implements BaseBusiness.HasBusiness {
        private String businessType;
        private Integer businessId;
    }

}
