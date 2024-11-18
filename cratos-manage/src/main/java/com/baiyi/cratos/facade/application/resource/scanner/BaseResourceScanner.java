package com.baiyi.cratos.facade.application.resource.scanner;

import com.baiyi.cratos.domain.generator.ApplicationResource;
import com.baiyi.cratos.service.ApplicationResourceService;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/18 10:37
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public abstract class BaseResourceScanner implements ResourceScanner {

    protected final EdsAssetIndexService edsAssetIndexService;
    protected final EdsAssetService edsAssetService;
    protected final ApplicationResourceService applicationResourceService;

    protected void saveResource(ApplicationResource applicationResource) {
        ApplicationResource resource = applicationResourceService.getByUniqueKey(applicationResource);
        if (resource == null) {
            applicationResourceService.add(applicationResource);
        } else {
            applicationResource.setId(resource.getId());
            applicationResourceService.updateByPrimaryKey(applicationResource);
        }
    }

}
