package com.baiyi.cratos.domain.param.http.eds;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/2/5 17:11
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class EdsInstanceParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class InstancePageQuery extends PageParam implements BusinessTagParam.HasQueryByTag {
        @Schema(description = "查询名称")
        private String queryName;
        private BusinessTagParam.QueryByTag queryByTag;
        private List<Integer> idList;
        @Schema(description = "Query by edsType")
        private String edsType;

        public InstancePageQueryParam toParam() {
            return InstancePageQueryParam.builder()
                    .page(getPage())
                    .length(getLength())
                    .queryName(queryName)
                    .idList(idList)
                    .edsType(edsType)
                    .build();
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class InstancePageQueryParam extends PageParam {
        private String queryName;
        private List<Integer> idList;
        private String edsType;
    }


    @Data
    @NoArgsConstructor
    @Schema
    public static class RegisterInstance implements IToTarget<EdsInstance> {
        // private Integer id;
        @NotNull(message = "Parameter 'instanceName' cannot be empty.")
        private String instanceName;
        // private String instanceType;
        private String kind;
        private String version;
        private Boolean valid;
        @NotNull(message = "Parameter 'configId' cannot be empty.")
        private Integer configId;
        private String url;
        private String comment;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class UpdateInstance implements IToTarget<EdsInstance> {
        private Integer id;
        @NotNull(message = "Parameter 'instanceName' cannot be empty.")
        private String instanceName;
        // private String instanceType;
        private String kind;
        private String version;
        private Boolean valid;
        private String url;
        private String comment;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class ImportInstanceAsset {
        @Schema(description = "Eds Instance ID")
        @NotNull
        private Integer instanceId;
        // private String instanceType;
        private String assetType;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class AssetPageQuery extends PageParam implements BusinessTagParam.HasQueryByTag {
        private Integer instanceId;
        @Schema(description = "Query by name")
        private String queryName;
        @Schema(description = "Query by assetType")
        private String assetType;
        private Boolean valid;
        private BusinessTagParam.QueryByTag queryByTag;
        private List<Integer> idList;

        public AssetPageQueryParam toParam() {
            return AssetPageQueryParam.builder()
                    .page(getPage())
                    .length(getLength())
                    .instanceId(this.instanceId)
                    .queryName(this.queryName)
                    .assetType(this.assetType)
                    .valid(this.valid)
                    .idList(idList)
                    .build();
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class AssetPageQueryParam extends PageParam {
        private Integer instanceId;
        private String queryName;
        private String assetType;
        private Boolean valid;
        private List<Integer> idList;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class QueryAssetByUniqueKey {
        @NotNull
        private Integer instanceId;
        @NotNull
        private String assetType;
        @NotNull
        private String assetKey;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class DeleteInstanceAsset {
        @Schema(description = "Eds Instance ID")
        private Integer instanceId;
        private String assetType;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class QueryCloudIdentityDetails {
        @NotBlank
        private String username;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class QueryLdapIdentityDetails {
        @NotBlank
        private String username;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class QueryDingtalkIdentityDetails {
        @NotBlank
        private String username;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class CommandExecInstancePageQuery extends PageParam {
        @Schema(description = "查询名称")
        private String queryName;
    }

}
