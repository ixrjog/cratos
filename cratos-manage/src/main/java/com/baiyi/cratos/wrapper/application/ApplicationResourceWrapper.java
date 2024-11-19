package com.baiyi.cratos.wrapper.application;

import com.baiyi.cratos.domain.generator.ApplicationResource;
import com.baiyi.cratos.domain.util.BeanCopierUtil;
import com.baiyi.cratos.domain.view.application.ApplicationResourceVO;
import com.baiyi.cratos.service.ApplicationResourceService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/15 11:50
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationResourceWrapper extends BaseDataTableConverter<ApplicationResourceVO.Resource, ApplicationResource> implements IBaseWrapper<ApplicationResourceVO.Resource> {

    private final ApplicationResourceService resourceService;

    @Override
    public void wrap(ApplicationResourceVO.Resource vo) {
    }

    //@Cacheable(cacheNames = CachingConfiguration.RepositoryName.LONG_TERM, key = "'APPLICATION:RESOURCES:NAME:' + #hasApplicationResources.applicationName", unless = "#result == null")
    public void wrap(ApplicationResourceVO.HasApplicationResources hasApplicationResources) {
        if (StringUtils.hasText(hasApplicationResources.getApplicationName())) {
            List<ApplicationResource> applicationResources = resourceService.queryByApplicationName(
                    hasApplicationResources.getApplicationName());
            if (!CollectionUtils.isEmpty(applicationResources)) {
                Map<String, List<ApplicationResourceVO.Resource>> resources = BeanCopierUtil.copyListProperties(
                                applicationResources, ApplicationResourceVO.Resource.class)
                        .stream()
                        .collect(Collectors.groupingBy(ApplicationResourceVO.Resource::getResourceType));
                hasApplicationResources.setResources(resources);
            }
        }
    }

//    @CacheEvict(cacheNames = CachingConfiguration.RepositoryName.LONG_TERM, key = "'APPLICATION:RESOURCES:NAME:' + #scanResource.name")
//    public void clean(ApplicationParam.ScanResource scanResource) {
//    }

}