package com.baiyi.cratos.workorder.builder.entry;

import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.ApplicationDeploymentModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.google.common.base.Joiner;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/5 14:06
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationDeploymentJvmSpecTicketEntryBuilder {

    private WorkOrderTicketParam.AddDeploymentJvmSpecTicketEntry param;

    public static ApplicationDeploymentJvmSpecTicketEntryBuilder newBuilder() {
        return new ApplicationDeploymentJvmSpecTicketEntryBuilder();
    }

    public ApplicationDeploymentJvmSpecTicketEntryBuilder withParam(
            WorkOrderTicketParam.AddDeploymentJvmSpecTicketEntry param) {
        this.param = param;
        return this;
    }

    public WorkOrderTicketEntry buildEntry() {
        ApplicationDeploymentModel.DeploymentJvmSpec deploymentJvmSpec = param.getDetail();
        final String applicationName = deploymentJvmSpec.getApplication()
                .getApplicationName();
        final String instanceName = deploymentJvmSpec.getEdsInstance()
                .getInstanceName();
        final String namespace = deploymentJvmSpec.getNamespace();
        final String deploymentName = deploymentJvmSpec.getDeployment()
                .getName();
        final String key = Joiner.on(":")
                .join(applicationName, instanceName, namespace, deploymentName);
        return WorkOrderTicketEntry.builder()
                .ticketId(param.getTicketId())
                .name(key)
                .displayName(key + ":" + deploymentJvmSpec.getJvmSpecType())
                .instanceId(deploymentJvmSpec.getEdsInstance()
                                    .getId())
                .businessType(param.getBusinessType())
                .businessId(deploymentJvmSpec.getDeployment()
                                    .getBusinessId())
                .completed(false)
                .namespace(namespace)
                .entryKey(key)
                .subType(deploymentJvmSpec.getJvmSpecType())
                .valid(true)
                .content(YamlUtils.dump(param.getDetail()))
                .build();
    }

}
