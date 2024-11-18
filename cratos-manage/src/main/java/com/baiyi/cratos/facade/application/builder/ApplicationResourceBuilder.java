package com.baiyi.cratos.facade.application.builder;

import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.*;

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

    private EdsInstance edsInstance;

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

    public ApplicationResourceBuilder withEdsInstance(EdsInstance edsInstance) {
        this.edsInstance = edsInstance;
        return this;
    }

    public ApplicationResource build() {
        return switch (this.type) {
            case KUBERNETES_RESOURCE -> buildWithKubernetesResource();
            case REPOSITORY_RESOURCE -> buildWithRepositoryResource();
            case null -> null;
        };
    }

    private ApplicationResource.ApplicationResourceBuilder init() {
        return ApplicationResource.builder()
                .applicationName(application.getName())
                .resourceType(edsAsset.getAssetType())
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .businessId(edsAsset.getId())
                .instanceName(edsInstance.getInstanceName())
                .name(edsAsset.getName())
                .displayName(edsAsset.getName())
                .comment(edsAsset.getDescription());
    }

    private ApplicationResource buildWithKubernetesResource() {
        return init().namespace(Optional.ofNullable(namespaceIndex)
                        .map(EdsAssetIndex::getValue)
                        .orElse(null))
                .build();
    }

    private ApplicationResource buildWithRepositoryResource() {
        return init().displayName(sshUrlIndex.getValue())
                .build();
    }

}
