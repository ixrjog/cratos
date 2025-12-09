package com.baiyi.cratos.workorder.entry.impl;

import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.common.util.MarkdownUtils;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.model.ApplicationDeploymentModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.util.BeanCopierUtils;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.domain.view.application.ApplicationVO;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.kubernetes.repo.template.KubernetesDeploymentRepo;
import com.baiyi.cratos.eds.kubernetes.util.KubeUtils;
import com.baiyi.cratos.exception.DaoServiceException;
import com.baiyi.cratos.service.ApplicationService;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.ApplicationDeploymentJvmSpecTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.DeploymentJvmSpecTypes;
import com.baiyi.cratos.workorder.enums.TableHeaderConstants;
import com.baiyi.cratos.workorder.enums.TicketState;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import com.baiyi.cratos.workorder.util.JvmSpecUtils;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.ResourceRequirements;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.KUBERNETES_NAMESPACE;
import static java.util.Map.entry;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/5 11:12
 * &#064;Version 1.0
 */
@Component
@BusinessType(type = BusinessTypeEnum.EDS_ASSET)
@WorkOrderKey(key = WorkOrderKeys.APPLICATION_DEPLOYMENT_JVM_SPEC)
public class ApplicationDeploymentJvmSpecTicketEntryProvider extends BaseTicketEntryProvider<ApplicationDeploymentModel.DeploymentJvmSpec, WorkOrderTicketParam.AddDeploymentJvmSpecTicketEntry> {

    private final EdsInstanceService edsInstanceService;
    private final ApplicationService applicationService;
    private final EdsAssetService edsAssetService;
    private final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;
    private final KubernetesDeploymentRepo kubernetesDeploymentRepo;
    private final EdsAssetIndexFacade edsAssetIndexFacade;
    private final EdsAssetIndexService edsAssetIndexService;

    public ApplicationDeploymentJvmSpecTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                           WorkOrderTicketService workOrderTicketService,
                                                           WorkOrderService workOrderService,
                                                           EdsInstanceService edsInstanceService,
                                                           ApplicationService applicationService,
                                                           EdsAssetService edsAssetService,
                                                           EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder,
                                                           KubernetesDeploymentRepo kubernetesDeploymentRepo,
                                                           EdsAssetIndexFacade edsAssetIndexFacade,
                                                           EdsAssetIndexService edsAssetIndexService) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.edsInstanceService = edsInstanceService;
        this.applicationService = applicationService;
        this.edsAssetService = edsAssetService;
        this.edsInstanceProviderHolderBuilder = edsInstanceProviderHolderBuilder;
        this.kubernetesDeploymentRepo = kubernetesDeploymentRepo;
        this.edsAssetIndexFacade = edsAssetIndexFacade;
        this.edsAssetIndexService = edsAssetIndexService;
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return MarkdownUtils.createTableHeader(TableHeaderConstants.APPLICATION_DEPLOYMENT_JVM_SPEC);
    }

    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        ApplicationDeploymentModel.DeploymentJvmSpec deploymentJvmSpec = loadAs(entry);
        EdsInstance instance = edsInstanceService.getById(entry.getInstanceId());
        String instanceName = Objects.nonNull(instance) ? instance.getInstanceName() : "N/A";
        //  String APPLICATION_DEPLOYMENT_JVM_SPEC = "| Application Name | Instance Name | Namespace | Deployment | Spec | New Java Opts |";
        return MarkdownUtils.createTableRow(
                deploymentJvmSpec.getApplicationName(), instance.getInstanceName(), deploymentJvmSpec.getNamespace(),
                deploymentJvmSpec.getDeployment()
                        .getName(), deploymentJvmSpec.getJvmSpecType(), deploymentJvmSpec.getModifiedJavaOpts()
        );
    }

    @Override
    public TicketEntryModel.EntryDesc getEntryDesc(WorkOrderTicketEntry entry) {
        ApplicationDeploymentModel.DeploymentJvmSpec deploymentJvmSpec = loadAs(entry);
        return TicketEntryModel.EntryDesc.builder()
                .name(entry.getName())
                .namespaces(entry.getNamespace())
                .desc(deploymentJvmSpec.getJvmSpecType())
                .build();
    }

    @Override
    public WorkOrderTicketEntry addEntry(WorkOrderTicketParam.AddDeploymentJvmSpecTicketEntry param) {
        WorkOrderTicketEntry entry = paramToEntry(param);
        WorkOrderTicket ticket = workOrderTicketService.getById(entry.getTicketId());
        if (!TicketState.COMPLETED.equals(TicketState.valueOf(ticket.getTicketState()))) {
            WorkOrderTicketException.runtime("Only when the work order is completed can the configuration be added.");
        }
        try {
            workOrderTicketEntryService.add(entry);
            return entry;
        } catch (DaoServiceException daoServiceException) {
            throw new WorkOrderTicketException("Repeat adding entries.");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                ApplicationDeploymentModel.DeploymentJvmSpec deploymentJvmSpec) throws WorkOrderTicketException {
        EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Deployment> holder = (EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Deployment>) edsInstanceProviderHolderBuilder.newHolder(
                entry.getInstanceId(), EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
        EdsKubernetesConfigModel.Kubernetes kubernetes = holder.getInstance()
                .getEdsConfigModel();
        Deployment deployment = kubernetesDeploymentRepo.get(
                kubernetes, deploymentJvmSpec.getNamespace(), deploymentJvmSpec.getDeployment()
                        .getAssetKey()
        );
        Container container = KubeUtils.findAppContainerOf(deployment)
                .orElseThrow(() -> new WorkOrderTicketException("Kubernetes application container not found."));
        EnvVar envVar = container.getEnv()
                .stream()
                .filter(e -> "JAVA_OPTS".equals(e.getName()))
                .findFirst()
                .orElseThrow(() -> new WorkOrderTicketException(
                        "Container environment variable JAVA_OPTS not found in Kubernetes deployment."));
        DeploymentJvmSpecTypes jvmSpecType = DeploymentJvmSpecTypes.valueOf(deploymentJvmSpec.getJvmSpecType());
        envVar.setValue(JvmSpecUtils.toCommandLine(JvmSpecUtils.parse(jvmSpecType, envVar.getValue())));
        modifyResources(jvmSpecType, container.getResources());
    }

    private void modifyResources(DeploymentJvmSpecTypes jvmSpecType, ResourceRequirements resource) {
        ApplicationDeploymentModel.ResourceRequirements resourceRequirements = JvmSpecUtils.getResourceRequirements(
                jvmSpecType);
        Map<String, Quantity> requests = Map.ofEntries(
                entry("cpu", new Quantity(resourceRequirements.getRequests().get("cpu"))),
                entry("memory", new Quantity(resourceRequirements.getRequests().get("memory")))
        );
        resource.setRequests(requests);
        Map<String, Quantity> limits = Map.ofEntries(
                entry("cpu", new Quantity(resourceRequirements.getLimits().get("cpu"))),
                entry("memory", new Quantity(resourceRequirements.getLimits().get("memory")))
        );
        resource.setLimits(limits);
    }

    @Override
    public WorkOrderTicketEntry paramToEntry(WorkOrderTicketParam.AddDeploymentJvmSpecTicketEntry param) {
        ApplicationDeploymentModel.DeploymentJvmSpec deploymentJvmSpec = param.getDetail();
        // application
        String applicationName = Optional.of(param)
                .map(WorkOrderTicketParam.AddDeploymentJvmSpecTicketEntry::getDetail)
                .map(ApplicationDeploymentModel.DeploymentJvmSpec::getApplicationName)
                .orElseThrow(() -> new WorkOrderTicketException("Application name cannot be empty."));
        Application application = applicationService.getByName(applicationName);
        if (Objects.isNull(application)) {
            WorkOrderTicketException.runtime(StringFormatter.format("Application {} does not exist.", applicationName));
        }
        deploymentJvmSpec.setApplication(BeanCopierUtils.copyProperties(application, ApplicationVO.Application.class));
        // deployment
        if (!IdentityUtils.hasIdentity(deploymentJvmSpec.getAssetId())) {
            WorkOrderTicketException.runtime("Kubernetes deployment asset id cannot be empty.");
        }
        EdsAsset asset = edsAssetService.getById(deploymentJvmSpec.getAssetId());
        if (Objects.isNull(asset)) {
            WorkOrderTicketException.runtime("Kubernetes deployment asset does not exist.");
        }
        if (!EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name()
                .equals(asset.getAssetType())) {
            WorkOrderTicketException.runtime(
                    StringFormatter.format("Asset type {} is not kubernetes deployment", asset.getAssetType()));
        }
        deploymentJvmSpec.setDeployment(BeanCopierUtils.copyProperties(asset, EdsAssetVO.Asset.class));
        // namespace
        EdsAssetIndex index = edsAssetIndexService.getByAssetIdAndName(asset.getId(), KUBERNETES_NAMESPACE);
        if (index == null || !StringUtils.hasText(index.getValue())) {
            WorkOrderTicketException.runtime("Eds Kubernetes deployment namespace does not exist.");
        }
        deploymentJvmSpec.setNamespace(index.getValue());
        // eds instance
        EdsInstance edsInstance = edsInstanceService.getById(asset.getInstanceId());
        if (Objects.isNull(edsInstance)) {
            WorkOrderTicketException.runtime("Eds Kubernetes instance does not exist.");
        }
        deploymentJvmSpec.setEdsInstance(BeanCopierUtils.copyProperties(edsInstance, EdsInstanceVO.EdsInstance.class));
        // 校验规格 jvmSpecType
        try {
            DeploymentJvmSpecTypes jvmSpecType = DeploymentJvmSpecTypes.valueOf(deploymentJvmSpec.getJvmSpecType());
            deploymentJvmSpec.setModifiedJavaOpts(JvmSpecUtils.getSpecJvmArgs(jvmSpecType));
        } catch (IllegalArgumentException e) {
            WorkOrderTicketException.runtime("Invalid jvm spec type.");
        }
        return ApplicationDeploymentJvmSpecTicketEntryBuilder.newBuilder()
                .withParam(param)
                .buildEntry();
    }

}