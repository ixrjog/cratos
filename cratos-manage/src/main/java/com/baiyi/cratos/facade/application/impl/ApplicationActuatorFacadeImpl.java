package com.baiyi.cratos.facade.application.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.param.http.application.ApplicationActuatorParam;
import com.baiyi.cratos.domain.view.application.ApplicationActuatorVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.kubernetes.util.KubeUtil;
import com.baiyi.cratos.exception.DaoServiceException;
import com.baiyi.cratos.facade.BusinessTagFacade;
import com.baiyi.cratos.facade.TagFacade;
import com.baiyi.cratos.facade.application.ApplicationActuatorFacade;
import com.baiyi.cratos.facade.application.model.ApplicationActuatorModel;
import com.baiyi.cratos.service.*;
import com.baiyi.cratos.wrapper.application.ApplicationActuatorWrapper;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/23 15:52
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationActuatorFacadeImpl implements ApplicationActuatorFacade {

    private final ApplicationService applicationService;
    private final ApplicationActuatorService applicationActuatorService;
    private final ApplicationResourceService applicationResourceService;
    private final TagService tagService;
    private final TagFacade tagFacade;
    private final BusinessTagFacade businessTagFacade;
    private final BusinessTagService businessTagService;
    private final EdsAssetService edsAssetService;
    private final EdsInstanceProviderHolderBuilder holderBuilder;
    private final ApplicationActuatorWrapper applicationActuatorWrapper;

    @Override
    //@SchedulerLock(name = SchedulerLockNameConstants.SCAN_ALL_APPLICATION_ACTUATOR_TASK, lockAtMostFor = "10m", lockAtLeastFor = "10m")
    public void scanAll() {
        Tag frameworkTag = tagService.getByTagKey("Framework");
        List<Application> applications = applicationService.selectAll();
        Map<Integer, EdsInstanceProviderHolder<?, Deployment>> holders = Maps.newHashMap();
        applications.forEach(application -> {
            boolean isPPJFramework = businessTagFacade.containsTag(BusinessTypeEnum.APPLICATION.name(),
                    application.getId(), "Framework");
            if (isPPJFramework) {
                List<ApplicationResource> resources = applicationResourceService.queryApplicationResource(
                        application.getName(), EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
                if (!CollectionUtils.isEmpty(resources)) {
                    resources.forEach(
                            resource -> saveApplicationActuator(application, resource, frameworkTag, holders));
                }
            }
        });
    }

    private void saveApplicationActuator(Application application, ApplicationResource resource, Tag frameworkTag,
                                         Map<Integer, EdsInstanceProviderHolder<?, Deployment>> holders) {
        if (!BusinessTypeEnum.EDS_ASSET.name()
                .equals(resource.getBusinessType())) {
            return;
        }
        EdsAsset edsAsset = edsAssetService.getById(resource.getBusinessId());
        if (edsAsset == null) {
            return;
        }
        EdsInstanceProviderHolder<?, Deployment> holder = getHolder(edsAsset.getInstanceId(), holders);
        try {
            Deployment deployment = holder.getProvider()
                    .assetLoadAs(edsAsset.getOriginalModel());
            Optional<Container> optionalContainer = KubeUtil.findAppContainerOf(deployment);
            if (optionalContainer.isEmpty()) {
                return;
            }
            BusinessTag uniqueKey = BusinessTag.builder()
                    .businessType(BusinessTypeEnum.APPLICATION.name())
                    .businessId(application.getId())
                    .tagId(frameworkTag.getId())
                    .build();
            BusinessTag businessTag = businessTagService.getByUniqueKey(uniqueKey);
            ApplicationActuator applicationActuator = ApplicationActuator.builder()
                    .applicationName(application.getName())
                    .instanceName(resource.getInstanceName())
                    .name(resource.getName())
                    .displayName(resource.getDisplayName())
                    .resourceType(resource.getResourceType())
                    .businessType(resource.getBusinessType())
                    .businessId(resource.getBusinessId())
                    .namespace(resource.getNamespace())
                    .framework(Optional.ofNullable(businessTag)
                            .map(BusinessTag::getTagValue)
                            .orElse(null))
                    .lifecycle(ApplicationActuatorModel.to(optionalContainer.map(Container::getLifecycle)
                                    .orElse(null))
                            .dump())
                    .livenessProbe(ApplicationActuatorModel.to(optionalContainer.map(Container::getLivenessProbe)
                                    .orElse(null))
                            .dump())
                    .readinessProbe(ApplicationActuatorModel.to(optionalContainer.map(Container::getReadinessProbe)
                                    .orElse(null))
                            .dump())
                    .startupProbe(ApplicationActuatorModel.to(optionalContainer.map(Container::getStartupProbe)
                                    .orElse(null))
                            .dump())
                    .standard(false)
                    .build();
            save(applicationActuator);
        } catch (NullPointerException nullPointerException) {
            log.error(nullPointerException.getMessage());
        }
    }

    private void save(ApplicationActuator applicationActuator) {
        try {
            ApplicationActuator recode = applicationActuatorService.getByUniqueKey(applicationActuator);
            if (recode == null) {
                applicationActuatorService.add(applicationActuator);
            } else {
                recode.setLifecycle(applicationActuator.getLifecycle());
                recode.setLivenessProbe(applicationActuator.getLivenessProbe());
                recode.setReadinessProbe(applicationActuator.getReadinessProbe());
                recode.setStartupProbe(applicationActuator.getStartupProbe());
                recode.setStandard(applicationActuator.getStandard());
                applicationActuatorService.updateByPrimaryKey(recode);
            }
        } catch (DaoServiceException daoServiceException) {
            log.error(daoServiceException.getMessage());
        }
    }

    private EdsInstanceProviderHolder<?, Deployment> getHolder(int instanceId,
                                                               Map<Integer, EdsInstanceProviderHolder<?, Deployment>> holders) {
        if (holders.containsKey(instanceId)) {
            return holders.get(instanceId);
        }
        EdsInstanceProviderHolder<?, Deployment> holder = (EdsInstanceProviderHolder<?, Deployment>) holderBuilder.newHolder(
                instanceId, EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
        holders.put(instanceId, holder);
        return holder;
    }

    @Override
    public DataTable<ApplicationActuatorVO.ApplicationActuator> queryApplicationActuatorPage(
            ApplicationActuatorParam.ApplicationActuatorPageQuery pageQuery) {
        DataTable<ApplicationActuator> table = applicationActuatorService.queryApplicationActuatorPage(pageQuery);
        return applicationActuatorWrapper.wrapToTarget(table);
    }

}
