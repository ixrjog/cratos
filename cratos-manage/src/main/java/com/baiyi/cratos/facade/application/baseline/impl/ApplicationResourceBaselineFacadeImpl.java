package com.baiyi.cratos.facade.application.baseline.impl;

import com.baiyi.cratos.common.constants.SchedulerLockNameConstants;
import com.baiyi.cratos.common.enums.ResourceBaselineTypeEnum;
import com.baiyi.cratos.common.exception.ApplicationResourceBaselineException;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.param.http.application.ApplicationResourceBaselineParam;
import com.baiyi.cratos.domain.view.application.ApplicationResourceBaselineVO;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.kubernetes.repo.template.KubernetesDeploymentRepo;
import com.baiyi.cratos.eds.kubernetes.util.KubeUtil;
import com.baiyi.cratos.facade.BusinessTagFacade;
import com.baiyi.cratos.facade.TagFacade;
import com.baiyi.cratos.facade.application.ApplicationResourceBaselineFacade;
import com.baiyi.cratos.facade.application.ApplicationResourceBaselineRedeployingFacade;
import com.baiyi.cratos.facade.application.baseline.factory.BaselineMemberProcessorFactory;
import com.baiyi.cratos.facade.application.baseline.mode.converter.DeploymentBaselineConverter;
import com.baiyi.cratos.service.*;
import com.baiyi.cratos.wrapper.application.ApplicationResourceBaselineWrapper;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

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
    private final ApplicationResourceBaselineWrapper applicationResourceBaselineWrapper;
    private final ApplicationResourceBaselineMemberService baselineMemberService;
    private final ApplicationResourceService applicationResourceService;
    private final TagService tagService;
    private final TagFacade tagFacade;
    private final BusinessTagFacade businessTagFacade;
    private final BusinessTagService businessTagService;
    private final EdsAssetService edsAssetService;
    private final EdsInstanceProviderHolderBuilder holderBuilder;
    private final KubernetesDeploymentRepo kubernetesDeploymentRepo;
    private final ApplicationResourceBaselineRedeployingFacade deploymentRedeployFacade;

    private static final String TAG_FRAMEWORK = "Framework";

    @Override
    @SchedulerLock(name = SchedulerLockNameConstants.SCAN_ALL_APPLICATION_RESOURCE_BASELINE_TASK, lockAtMostFor = "3m", lockAtLeastFor = "3m")
    public void scanAll() {
        Tag frameworkTag = tagService.getByTagKey(TAG_FRAMEWORK);
        List<Application> applications = applicationService.selectAll();
        Map<Integer, EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Deployment>> holders = Maps.newHashMap();
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
    }

    @Override
    public void mergeToBaseline(int baselineId) {
        ApplicationResourceBaseline baseline = baselineService.getById(baselineId);
        if (baseline == null) {
            return;
        }
        EdsAsset edsAsset = edsAssetService.getById(baseline.getBusinessId());
        if (edsAsset == null) {
            return;
        }
        List<ApplicationResourceBaselineMember> baselineMembers = baselineMemberService.queryByBaselineId(baselineId);
        if (CollectionUtils.isEmpty(baselineMembers)) {
            return;
        }
        Map<Integer, EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Deployment>> holders = Maps.newHashMap();
        EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Deployment> holder = getHolder(
                edsAsset.getInstanceId(), holders);
        try {
            Deployment deployment = getKubernetesDeployment(holder, edsAsset);
            BaselineMemberProcessorFactory.mergeToBaseline(baseline, baselineMembers, deployment);
            Deployment updated = kubernetesDeploymentRepo.update(holder.getInstance()
                    .getEdsConfigModel(), deployment);
            // 写入资产
            holder.getProvider()
                    .importAsset(holder.getInstance(), updated);
            this.rescan(baselineId);
        } catch (NullPointerException nullPointerException) {
            log.error(nullPointerException.getMessage());
        }
    }

    private Deployment getKubernetesDeployment(EdsInstanceProviderHolder<?, Deployment> holder, EdsAsset edsAsset) {
        return holder.getProvider()
                .getAsset(edsAsset);
    }

    @Override
    public void rescan(int baselineId) {
        ApplicationResourceBaseline baseline = baselineService.getById(baselineId);
        if (baseline == null) {
            return;
        }
        // 重新检查标签
        checkAndUpdateBaselineFramework(baseline);
        EdsAsset edsAsset = edsAssetService.getById(baseline.getBusinessId());
        if (edsAsset == null) {
            return;
        }
        EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Deployment> holder = getHolder(
                edsAsset.getInstanceId());
        try {
            Deployment deployment = getKubernetesDeployment(holder, edsAsset);
            Optional<Container> optionalContainer = KubeUtil.findAppContainerOf(deployment);
            optionalContainer.ifPresent(container -> this.scan(baseline, container));
        } catch (NullPointerException nullPointerException) {
            log.error(nullPointerException.getMessage());
        }
    }

    private void checkAndUpdateBaselineFramework(ApplicationResourceBaseline baseline) {
        // 重新检查标签
        Application application = applicationService.getByName(baseline.getApplicationName());
        if (application == null) {
            ApplicationResourceBaselineException.runtime("Application does not exist.");
        }
        Tag frameworkTag = tagService.getByTagKey(TAG_FRAMEWORK);
        BusinessTag uniqueKey = BusinessTag.builder()
                .tagId(frameworkTag.getId())
                .businessType(BusinessTypeEnum.APPLICATION.name())
                .businessId(application.getId())
                .build();
        BusinessTag frameworkBizTag = businessTagService.getByUniqueKey(uniqueKey);
        if (!baseline.getFramework()
                .equals(frameworkBizTag.getTagValue())) {
            baseline.setFramework(frameworkBizTag.getTagValue());
            baselineService.updateByPrimaryKey(baseline);
        }
    }

    @Override
    public DataTable<ApplicationResourceBaselineVO.ResourceBaseline> queryApplicationResourceBaselinePage(
            ApplicationResourceBaselineParam.ApplicationResourceBaselinePageQuery pageQuery) {
        if (pageQuery.getByMemberType() != null) {
            try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
                Validator validator = factory.getValidator();
                Set<ConstraintViolation<ApplicationResourceBaselineParam.BaselineMember>> constraintViolations = validator.validate(
                        pageQuery.getByMemberType());
                if (!CollectionUtils.isEmpty(constraintViolations)) {
                    ApplicationResourceBaselineException.runtime(Joiner.on(",")
                            .join(constraintViolations.stream()
                                    .map(ConstraintViolation::getMessage)
                                    .toList()));
                }
            }
        }
        // 只作用于prod环境
        if (Objects.nonNull(pageQuery.getIsQueryCanary()) && !"prod".equals(pageQuery.getNamespace())) {
            pageQuery.setIsQueryCanary(null);
        }
        DataTable<ApplicationResourceBaseline> dataTable = baselineService.queryApplicationResourceBaselinePage(
                pageQuery);
        return applicationResourceBaselineWrapper.wrapToTarget(dataTable);
    }

    @Override
    public void redeploy(int baselineId) {
        ApplicationResourceBaseline baseline = baselineService.getById(baselineId);
        if (baseline == null) {
            ApplicationResourceBaselineException.runtime("Baseline configuration does not exist.");
        }
        if (deploymentRedeployFacade.isRedeploying(baselineId)) {
            ApplicationResourceBaselineException.runtime(
                    "Deploying, duplicate operations are prohibited within 1 minute.");
        }
        if (!BusinessTypeEnum.EDS_ASSET.name()
                .equals(baseline.getBusinessType())) {
            ApplicationResourceBaselineException.runtime("BusinessType is not an EDS_ASSET.");
        }
        EdsAsset edsAsset = edsAssetService.getById(baseline.getBusinessId());
        if (edsAsset == null) {
            ApplicationResourceBaselineException.runtime("Asset does not exist.");
        }
        EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Deployment> holder = getHolder(
                edsAsset.getInstanceId());
        try {
            Deployment deployment = getKubernetesDeployment(holder, edsAsset);
            kubernetesDeploymentRepo.redeploy(holder.getInstance()
                    .getEdsConfigModel(), deployment);
            deploymentRedeployFacade.deploying(baselineId);
        } catch (Exception ex) {
            ApplicationResourceBaselineException.runtime(ex.getMessage());
        }
    }

    private void saveBaseline(Application application, ApplicationResource resource, Tag frameworkTag,
                              Map<Integer, EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Deployment>> holders) {
        if (!BusinessTypeEnum.EDS_ASSET.name()
                .equals(resource.getBusinessType())) {
            return;
        }
        EdsAsset edsAsset = edsAssetService.getById(resource.getBusinessId());
        if (edsAsset == null) {
            return;
        }
        EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Deployment> holder = getHolder(
                edsAsset.getInstanceId(), holders);
        try {
            Deployment deployment = getKubernetesDeployment(holder, edsAsset);
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
            this.scan(baseline, optionalContainer.get());
        } catch (NullPointerException nullPointerException) {
            log.error(nullPointerException.getMessage());
        }
    }

    private void scan(ApplicationResourceBaseline baseline, Container container) {
        BaselineMemberProcessorFactory.saveAll(baseline, container);
        // 回写合规字段
        boolean standard = !baselineMemberService.hasNonStandardBaselineMember(baseline.getId());
        if (baseline.getStandard() != standard) {
            baseline.setStandard(standard);
            baselineService.updateByPrimaryKey(baseline);
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

    private EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Deployment> getHolder(int instanceId) {
        return getHolder(instanceId, Maps.newHashMap());
    }

    private EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Deployment> getHolder(int instanceId,
                                                                                                 Map<Integer, EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Deployment>> holders) {
        if (holders.containsKey(instanceId)) {
            return holders.get(instanceId);
        }
        EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Deployment> holder = (EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Deployment>) holderBuilder.newHolder(
                instanceId, EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
        holders.put(instanceId, holder);
        return holder;
    }

}
