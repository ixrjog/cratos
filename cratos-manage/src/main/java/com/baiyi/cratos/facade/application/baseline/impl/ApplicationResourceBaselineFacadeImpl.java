package com.baiyi.cratos.facade.application.baseline.impl;

import com.baiyi.cratos.common.enums.ResourceBaselineTypeEnum;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.kubernetes.util.KubeUtil;
import com.baiyi.cratos.facade.BusinessTagFacade;
import com.baiyi.cratos.facade.TagFacade;
import com.baiyi.cratos.facade.application.ApplicationResourceBaselineFacade;
import com.baiyi.cratos.facade.application.baseline.factory.BaselineMemberProcessorFactory;
import com.baiyi.cratos.facade.application.baseline.mode.converter.DeploymentBaselineConverter;
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
 * &#064;Date  2024/12/30 11:01
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationResourceBaselineFacadeImpl implements ApplicationResourceBaselineFacade {

    private final ApplicationService applicationService;
    private final ApplicationResourceBaselineService baselineService;
    private final ApplicationResourceBaselineMemberService baselineMemberService;
    private final ApplicationResourceService applicationResourceService;
    private final TagService tagService;
    private final TagFacade tagFacade;
    private final BusinessTagFacade businessTagFacade;
    private final BusinessTagService businessTagService;
    private final EdsAssetService edsAssetService;
    private final EdsInstanceProviderHolderBuilder holderBuilder;
    private final ApplicationActuatorWrapper applicationActuatorWrapper;

    private static final String TAG_FRAMEWORK = "Framework";

    @Override
    //@SchedulerLock(name = SchedulerLockNameConstants.SCAN_ALL_APPLICATION_RESOURCE_BASELINE_TASK, lockAtMostFor = "10m", lockAtLeastFor = "10m")
    public void scanAll() {
        Tag frameworkTag = tagService.getByTagKey(TAG_FRAMEWORK);
        List<Application> applications = applicationService.selectAll();
        Map<Integer, EdsInstanceProviderHolder<?, Deployment>> holders = Maps.newHashMap();
        applications.forEach(application -> {
            boolean hasFramework = businessTagFacade.containsTag(BusinessTypeEnum.APPLICATION.name(),
                    application.getId(), TAG_FRAMEWORK);
            if (hasFramework) {
                List<ApplicationResource> resources = applicationResourceService.queryApplicationResource(
                        application.getName(), EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
                if (!CollectionUtils.isEmpty(resources)) {
                    resources.forEach(resource -> saveBaseline(application, resource, frameworkTag, holders));
                }
            }
        });
        // TODO standard;
    }

    private void saveBaseline(Application application, ApplicationResource resource, Tag frameworkTag,
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
            ApplicationResourceBaseline baseline = ApplicationResourceBaseline.builder()
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
                    .standard(false)
                    .build();
            int baselineId = saveBaseline(baseline);
            baseline.setId(baselineId);
            BaselineMemberProcessorFactory.saveMemberAll(baseline,optionalContainer.get());


//                    .lifecycle(DeploymentBaselineModel.to(optionalContainer.map(Container::getLifecycle)
//                                    .orElse(null))
//                            .dump())
//                    .livenessProbe(DeploymentBaselineModel.to(optionalContainer.map(Container::getLivenessProbe)
//                                    .orElse(null))
//                            .dump())
//                    .readinessProbe(DeploymentBaselineModel.to(optionalContainer.map(Container::getReadinessProbe)
//                                    .orElse(null))
//                            .dump())
//                    .startupProbe(DeploymentBaselineModel.to(optionalContainer.map(Container::getStartupProbe)
//                                    .orElse(null))
//                            .dump())

        } catch (NullPointerException nullPointerException) {
            log.error(nullPointerException.getMessage());
        }
    }

    private void saveBaselineMember(ApplicationResourceBaseline baseline, Container container) {
        ApplicationResourceBaselineMember lifecycleMember = ApplicationResourceBaselineMember.builder()
                .baselineId(baseline.getId())
                .baselineType(ResourceBaselineTypeEnum.CONTAINER_LIFECYCLE.name())
                .name(ResourceBaselineTypeEnum.CONTAINER_LIFECYCLE.getDisplayName())
                .content(DeploymentBaselineConverter.to(Optional.of(container)
                                .map(Container::getLifecycle)
                                .orElse(null))
                        .dump())
                .standard(false)
                .build();
    }

    /**
     * @param baseline
     * @return id
     */
    private int saveBaseline(ApplicationResourceBaseline baseline) {
        ApplicationResourceBaseline recode = baselineService.getByUniqueKey(baseline);
        if (recode == null) {
            baselineService.add(baseline);
            return baseline.getId();
        }
        return recode.getId();
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

}
