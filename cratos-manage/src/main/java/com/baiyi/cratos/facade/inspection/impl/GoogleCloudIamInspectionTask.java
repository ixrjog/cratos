package com.baiyi.cratos.facade.inspection.impl;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.util.PasswordGenerator;
import com.baiyi.cratos.common.util.beetl.BeetlUtil;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.domain.util.JSONUtils;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.config.model.EdsGcpConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsProviderHolderFactory;
import com.baiyi.cratos.eds.core.support.EdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.core.util.SreBridgeUtils;
import com.baiyi.cratos.eds.core.util.SreEventFormatter;
import com.baiyi.cratos.eds.googlecloud.enums.GcpIAMMemberType;
import com.baiyi.cratos.eds.googlecloud.model.GcpMemberModel;
import com.baiyi.cratos.facade.inspection.base.BaseEdsInspectionTask;
import com.baiyi.cratos.facade.inspection.context.InspectionTaskContext;
import com.baiyi.cratos.facade.inspection.model.GoogleCloudModel;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.baiyi.cratos.common.enums.NotificationTemplateKeys.GCP_IAM_UNKNOWN_IDENTITY_INSPECTION_NOTICE;
import static com.baiyi.cratos.eds.core.util.SreEventFormatter.EVENT_ID;
import static java.util.Map.entry;

/**
 * 巡检 GCP 中未关联员工的 IAM 账户
 * &#064;Author  baiyi
 * &#064;Date  2026/3/26 10:05
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
@Slf4j
@Component
public class GoogleCloudIamInspectionTask extends BaseEdsInspectionTask<EdsConfigs.Gcp> {

    private final BusinessTagFacade businessTagFacade;
    private static final String MEMBERS_FIELD = "members";

    public GoogleCloudIamInspectionTask(InspectionTaskContext context,
                                        EdsProviderHolderFactory edsProviderHolderFactory,
                                        EdsInstanceService edsInstanceService, ConfigCredTemplate configCredTemplate,
                                        CredentialService credentialService, EdsAssetService edsAssetService,
                                        BusinessTagFacade businessTagFacade) {
        super(
                context, edsProviderHolderFactory, edsInstanceService, configCredTemplate, credentialService,
                edsAssetService
        );
        this.businessTagFacade = businessTagFacade;
    }

    @Override
    protected String getMsg() throws IOException {
        List<EdsInstance> instances = context.getEdsInstanceQueryHelper()
                .queryInstance(EdsInstanceTypeEnum.GCP);
        List<GoogleCloudModel.IamMember> iamMembers = Lists.newArrayList();
        instances.forEach(instance -> iamMembers.addAll(queryGcpMembers(instance)));
        NotificationTemplate notificationTemplate = getNotificationTemplate(GCP_IAM_UNKNOWN_IDENTITY_INSPECTION_NOTICE);
        return BeetlUtil.renderTemplate(
                notificationTemplate.getContent(), SimpleMapBuilder.newBuilder()
                        .put(MEMBERS_FIELD, iamMembers)
                        .build()
        );
    }

    private List<GoogleCloudModel.IamMember> queryGcpMembers(EdsInstance instance) {
        List<EdsAsset> assets = edsAssetService.queryInstanceAssets(
                instance.getId(), EdsAssetTypeEnum.GCP_MEMBER.name());
        if (CollectionUtils.isEmpty(assets)) {
            return List.of();
        }
        List<GoogleCloudModel.IamMember> iamMembers = Lists.newArrayList();
        EdsInstanceProviderHolder<EdsConfigs.Gcp, GcpMemberModel.Member> holder = (EdsInstanceProviderHolder<EdsConfigs.Gcp, GcpMemberModel.Member>) edsProviderHolderFactory.createHolder(
                instance.getId(), EdsAssetTypeEnum.GCP_MEMBER.name());
        EdsInstanceAssetProvider<EdsConfigs.Gcp, GcpMemberModel.Member> provider = holder.getProvider();
        assets.forEach(asset -> {
            try {
                GcpMemberModel.Member member = provider.loadAsset(asset.getOriginalModel());
                if (GcpIAMMemberType.USER.getKey()
                        .equals(member.getType())) {
                    SimpleBusiness simpleBusiness = SimpleBusiness.builder()
                            .businessType(BusinessTypeEnum.EDS_ASSET.name())
                            .businessId(asset.getId())
                            .build();
                    BusinessTag businessTag = businessTagFacade.getBusinessTag(
                            simpleBusiness, SysTagKeys.USERNAME.getKey());
                    if (businessTag == null) {
                        iamMembers.add(GoogleCloudModel.IamMember.builder()
                                               .instanceName(instance.getInstanceName())
                                               .member(member.getName())
                                               .build());
                        // SRE Event
                        publish(
                                holder.getInstance()
                                        .getConfig(), member
                        );
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
        return iamMembers;
    }

    private void publish(EdsConfigs.Gcp gcp, GcpMemberModel.Member member) {
        try {
            String env = "prod";
            Map<String, String> ext = Map.of(EVENT_ID, PasswordGenerator.generateNo());
            String projectName = Optional.ofNullable(gcp)
                    .map(EdsConfigs.Gcp::getProject)
                    .map(EdsGcpConfigModel.Project::getName)
                    .orElse("--");
            String projectId = Optional.ofNullable(gcp)
                    .map(EdsConfigs.Gcp::getProject)
                    .map(EdsGcpConfigModel.Project::getId)
                    .orElse("--");
            Map<String, String> targetContent = Map.ofEntries(
                    entry("member", member.getName()), entry("projectName", projectName), entry("projectId", projectId),
                    entry("roles", JSONUtils.writeValueAsString(member.getRoles()))
            );
            com.baiyi.cratos.domain.model.SreBridgeModel.Event event = com.baiyi.cratos.domain.model.SreBridgeModel.Event.builder()
                    .operator(OPERATOR)
                    .action(SreEventFormatter.Action.INSPECT_GCP_IAM.getValue())
                    .description(StringFormatter.arrayFormat(
                            "An unknown member {} has been added to the Google Cloud project {}.", member.getName(),
                            projectName
                    ))
                    .target(member.getName())
                    .targetContent(SreEventFormatter.mapToJson(targetContent))
                    .affection("")
                    .severity("low")
                    .status("executed")
                    .type(SreEventFormatter.Type.INSPECTION.getValue())
                    .env(env)
                    .ext(ext)
                    .build();
            SreBridgeUtils.publish(event);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
    }

}
