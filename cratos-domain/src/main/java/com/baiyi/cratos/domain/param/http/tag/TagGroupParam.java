package com.baiyi.cratos.domain.param.http.tag;

import com.baiyi.cratos.domain.HasSessionUser;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/2 14:52
 * &#064;Version 1.0
 */
public class TagGroupParam {

    @Data
    @Schema
    public static class GetGroupOptions {
        private String queryName;
    }

    @Data
    @Schema
    public static class GetMyGroupOptions implements HasSessionUser {
        private String queryName;
        @Null
        private String username;

        @Override
        public void setSessionUser(String username) {
            this.username = username;
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class GroupAssetPageQuery extends PageParam {
        @NotBlank
        private String tagGroup;
        private String queryName;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class MyGroupAssetPageQuery extends PageParam implements HasSessionUser {
        @NotBlank
        private String tagGroup;
        private String queryName;
        @Null
        private String username;

        @Override
        public void setSessionUser(String username) {
            this.username = username;
        }
    }

}
