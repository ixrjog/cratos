package com.baiyi.cratos.domain.param.http.user;

import com.baiyi.cratos.domain.param.PageParam;
import com.baiyi.cratos.domain.param.http.commit.CommitParam;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/5 15:53
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class UserExtParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class UserExtPageQuery extends PageParam implements BusinessTagParam.HasQueryByTag {
        @Schema(description = "查询名称")
        private String queryName;
        private BusinessTagParam.QueryByTag queryByTag;
        private List<Integer> idList;
        private Integer extUserTagId;
        private Boolean valid;

        public UserExtPageQueryParam toParam() {
            return UserExtPageQueryParam.builder()
                    .page(getPage())
                    .length(getLength())
                    .queryName(queryName)
                    .idList(idList)
                    .valid(valid)
                    .extUserTagId(extUserTagId)
                    .build();
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class UserExtPageQueryParam extends PageParam {
        private String queryName;
        private List<Integer> idList;
        private Integer extUserTagId;
        private Boolean valid;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class RenewalExtUser {
        @NotBlank
        private String username;
        @NotBlank
        private String renewalType;
        @Schema(description = "是否续期所有; true:续签所有, false:只续签账户")
        private Boolean renewalOfAll;
        private CommitParam.Commit commit;
    }

}
