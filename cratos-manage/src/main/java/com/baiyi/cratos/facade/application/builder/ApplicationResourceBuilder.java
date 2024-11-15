package com.baiyi.cratos.facade.application.builder;

import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.generator.ApplicationResource;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;

import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/15 15:06
 * &#064;Version 1.0
 */
public class ApplicationResourceBuilder {

    public enum Type {
        KUBERNETES_RESOURCE,
        REPOSITORY_RESOURCE
    }

    private Application application;

    private EdsAsset edsAsset;

    private EdsAssetIndex namespaceIndex;

    private EdsAssetIndex sshUrlIndex;

    private Type type;

    public static ApplicationResourceBuilder newBuilder() {
        return new ApplicationResourceBuilder();
    }

    public ApplicationResourceBuilder withApplication(Application application) {
        this.application = application;
        return this;
    }

    public ApplicationResourceBuilder withEdsAsset(EdsAsset edsAsset) {
        this.edsAsset = edsAsset;
        return this;
    }

    public ApplicationResourceBuilder withNamespaceIndex(EdsAssetIndex namespaceIndex) {
        this.namespaceIndex = namespaceIndex;
        return this;
    }

    public ApplicationResourceBuilder withType(Type type) {
        this.type = type;
        return this;
    }

    public ApplicationResourceBuilder withSshUrlIndex(EdsAssetIndex sshUrlIndex) {
        this.sshUrlIndex = sshUrlIndex;
        return this;
    }

    public ApplicationResource build() {
        return switch (this.type) {
            case KUBERNETES_RESOURCE -> buildWithKubernetesResource();
            case REPOSITORY_RESOURCE -> buildWithRepositoryResource();
            case null -> null;
        };
    }

    private ApplicationResource buildWithKubernetesResource() {
        return ApplicationResource.builder()
                .applicationName(application.getName())
                .resourceType(edsAsset.getAssetType())
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .businessId(edsAsset.getId())
                .name(edsAsset.getName())
                .namespace(Optional.ofNullable(namespaceIndex)
                        .map(EdsAssetIndex::getValue)
                        .orElse(null))
                .comment(edsAsset.getDescription())
                .build();
    }

    private ApplicationResource buildWithRepositoryResource() {
        return ApplicationResource.builder()
                .applicationName(application.getName())
                .resourceType(edsAsset.getAssetType())
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .businessId(edsAsset.getId())
                .name(sshUrlIndex.getValue())
                .comment(edsAsset.getDescription())
                .build();
    }

}
