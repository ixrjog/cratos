package com.baiyi.cratos.facade.application.builder;

import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.generator.ApplicationResource;
import com.baiyi.cratos.domain.generator.EdsAsset;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/15 15:06
 * &#064;Version 1.0
 */
public class ApplicationResourceBuilder {

    private Application application;

    private EdsAsset edsAsset;

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

    public ApplicationResource build() {
        return ApplicationResource.builder()
                .applicationName(application.getName())
                .resourceType(edsAsset.getAssetType())
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .businessId(edsAsset.getId())
                .name(edsAsset.getName())
                .comment(edsAsset.getDescription())
                .build();
    }

}
