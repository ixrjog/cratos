package com.baiyi.cratos.domain.param.kubernetes;

import com.baiyi.cratos.domain.HasSessionUser;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.KubernetesResourceTemplate;
import com.baiyi.cratos.domain.generator.KubernetesResourceTemplateMember;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import com.baiyi.cratos.domain.param.tag.BusinessTagParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/1 14:09
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class KubernetesResourceTemplateParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class TemplatePageQuery extends PageParam implements BusinessTagParam.HasQueryByTag {
        @Schema(description = "查询名称")
        private String queryName;
        private BusinessTagParam.QueryByTag queryByTag;
        private List<Integer> idList;

        public TemplatePageQueryParam toParam() {
            return TemplatePageQueryParam.builder()
                    .page(getPage())
                    .length(getLength())
                    .queryName(queryName)
                    .idList(idList)
                    .build();
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class TemplatePageQueryParam extends PageParam {
        private String queryName;
        private List<Integer> idList;
    }

    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class CopyTemplate {
        private Integer templateId;
        private String templateName;
        private String templateKey;
    }

    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class CreateResourceByTemplate implements HasSessionUser {
        private Integer templateId;
        private String custom;
        private String createdBy;

        @Override
        public void setSessionUser(String username) {
            this.createdBy = username;
        }
    }

    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.KUBERNETES_RESOURCE_TEMPLATE)
    public static class AddTemplate implements IToTarget<KubernetesResourceTemplate> {
        private String name;
        private String templateKey;
        private String apiVersion;
        private Boolean valid;
        private String custom;
        private String comment;
    }

    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.KUBERNETES_RESOURCE_TEMPLATE)
    public static class UpdateTemplate implements IToTarget<KubernetesResourceTemplate> {
        private Integer id;
        private String name;
        private String templateKey;
        private String apiVersion;
        private Boolean valid;
        private String custom;
        private String comment;
    }

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class MemberPageQuery extends PageParam {
        @NotNull
        private Integer templateId;
        private String namespace;
        private String kind;
        private String queryName;
    }

    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.KUBERNETES_RESOURCE_TEMPLATE_MEMBER)
    public static class AddMember implements IToTarget<KubernetesResourceTemplateMember> {
        @NotNull
        private Integer templateId;
        @NotBlank
        private String namespace;
        @NotBlank
        private String kind;
        @NotBlank
        private Boolean valid;
        private String content;
        private String custom;
        private String comment;
    }

    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.KUBERNETES_RESOURCE_TEMPLATE_MEMBER)
    public static class UpdateMember implements IToTarget<KubernetesResourceTemplateMember> {
        @NotNull
        private Integer id;
        @NotNull
        private Integer templateId;
        @NotBlank
        private String namespace;
        private String kind;
        @NotBlank
        private Boolean valid;
        @NotBlank
        private String content;
        private String custom;
        private String comment;
    }

}
