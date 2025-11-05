package com.baiyi.cratos.facade.eaglecloud.impl;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.http.event.EagleCloudEventParam;
import com.baiyi.cratos.eds.core.config.EdsEagleCloudConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.eaglecloud.sase.model.EagleCloudModel;
import com.baiyi.cratos.facade.eaglecloud.DataSecurityAlertSender;
import com.baiyi.cratos.facade.eaglecloud.EagleCloudSaseFacade;
import com.baiyi.cratos.facade.eaglecloud.EdsEagleCloudSaseInstanceManager;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.DINGTALK_MANAGER_USER_ID;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/29 15:04
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
@Slf4j
@Component
@AllArgsConstructor
public class EagleCloudSaseFacadeImpl implements EagleCloudSaseFacade {

    private final EdsEagleCloudSaseInstanceManager eagleCloudSaseInstanceManager;
    private final EdsInstanceProviderHolderBuilder holderBuilder;
    private final EdsAssetService edsAssetService;
    private final EdsAssetIndexService edsAssetIndexService;
    private final DataSecurityAlertSender dataSecurityAlertSender;

    @Override
    public void consumeEvent(EagleCloudEventParam.SaseHook saseHook, String hookToken) {
        if (StringUtils.isAnyBlank(saseHook.getContent(), hookToken)) {
            log.debug("Invalid event: missing event content or hook token.");
            return;
        }
        EdsInstance edsInstance = eagleCloudSaseInstanceManager.findInstanceByHookToken(hookToken);
        if (Objects.isNull(edsInstance)) {
            log.debug("No matching instance found for the provided hook token.");
            return;
        }
        // 录入事件
        EdsInstanceProviderHolder<EdsEagleCloudConfigModel.Sase, EagleCloudEventParam.SaseHook> eventHolder = (EdsInstanceProviderHolder<EdsEagleCloudConfigModel.Sase, EagleCloudEventParam.SaseHook>) holderBuilder.newHolder(
                edsInstance.getId(), EdsAssetTypeEnum.EAGLECLOUD_SASE_DATA_SECURITY_EVENT.name());
        if (eventHolder != null) {
            eventHolder.importAsset(saseHook);
        }
        EagleCloudEventParam.Content content = EagleCloudEventParam.Content.parse(saseHook);
        if (content == null || !org.springframework.util.StringUtils.hasText(content.getEntityName())) {
            return;
        }
        List<EdsAsset> dingtalkUsers = edsAssetService.queryByTypeAndName(EdsAssetTypeEnum.DINGTALK_USER.name(),
                content.getEntityName(), false);
        // 没找到钉钉用户
        if (CollectionUtils.isEmpty(dingtalkUsers)) {
            return;
        }
        EdsAsset dingtalkUser = dingtalkUsers.getFirst();
        // 查询主管
        List<EdsAsset> dingtalkManagers = queryDingtalkManagers(eventHolder.getInstance()
                .getEdsConfigModel(), dingtalkUser);
        sendAlert(edsInstance, saseHook, content, dingtalkManagers);
    }

    private List<EdsAsset> queryDingtalkManagers(EdsEagleCloudConfigModel.Sase sase, EdsAsset dingtalkUser) {
        EdsAssetIndex dingtalkManagerUserIdIndex = edsAssetIndexService.getByAssetIdAndName(dingtalkUser.getId(),
                DINGTALK_MANAGER_USER_ID);
        // 没有主管
        if (dingtalkManagerUserIdIndex != null) {
            return edsAssetService.queryInstanceAssetsById(dingtalkUser.getInstanceId(),
                    EdsAssetTypeEnum.DINGTALK_USER.name(), dingtalkManagerUserIdIndex.getValue());
        }
        // 从数据源配置中获取安全管理员
        List<EdsEagleCloudConfigModel.SecurityAdministrator> securityAdministrators = Optional.of(sase)
                .map(EdsEagleCloudConfigModel.Sase::getSecurityAdministrators)
                .orElse(List.of());
        if (CollectionUtils.isEmpty(securityAdministrators)) {
            return List.of();
        }
        // 遍历安全管理员
        return securityAdministrators.stream()
                .filter(sa -> org.springframework.util.StringUtils.hasText(sa.getDingtalkUserId()))
                .map(sa -> edsAssetService.queryInstanceAssetsById(0, EdsAssetTypeEnum.DINGTALK_USER.name(),
                        sa.getDingtalkUserId()))
                .filter(managers -> !CollectionUtils.isEmpty(managers))
                .flatMap(List::stream)
                .collect(java.util.stream.Collectors.toList());
    }

    private void sendAlert(EdsInstance edsInstance, EagleCloudEventParam.SaseHook saseHook,
                           EagleCloudEventParam.Content content, List<EdsAsset> dingtalkManagers) {
        // 没有主管
        if (CollectionUtils.isEmpty(dingtalkManagers)) {
            return;
        }
        dingtalkManagers.forEach(dingtalkManager -> sendAlert(edsInstance, saseHook, content, dingtalkManager));
    }

    private void sendAlert(EdsInstance edsInstance, EagleCloudEventParam.SaseHook saseHook,
                           EagleCloudEventParam.Content content, EdsAsset dingtalkManager) {
        EagleCloudEventParam.Receiver receiver = EagleCloudEventParam.Receiver.builder()
                .dingtalkUserId(dingtalkManager.getAssetId())
                .displayName(dingtalkManager.getName())
                .build();
        EagleCloudEventParam.Alert alert = EagleCloudEventParam.Alert.builder()
                .actionUrl(saseHook.getActionUrl())
                .title(saseHook.getTitle())
                .dataTime(saseHook.getDataTime())
                .timestamp(saseHook.getTimestamp())
                .text(saseHook.getContent())
                .content(content)
                .receiver(receiver)
                .build();
        // 发送告警
        sendAlert(edsInstance, dingtalkManager, alert);
        // 录入告警
        EdsInstanceProviderHolder<EdsEagleCloudConfigModel.Sase, EagleCloudEventParam.Alert> alertHolder = (EdsInstanceProviderHolder<EdsEagleCloudConfigModel.Sase, EagleCloudEventParam.Alert>) holderBuilder.newHolder(
                edsInstance.getId(), EdsAssetTypeEnum.EAGLECLOUD_SASE_DATA_SECURITY_ALERT_NOTIFICATION.name());
        if (alertHolder != null) {
            alertHolder.importAsset(alert);
        }
    }

    private void sendAlert(EdsInstance edsInstance, EdsAsset dingtalkManager, EagleCloudEventParam.Alert alert) {
        try {
            EagleCloudModel.AlertRecord alertRecord = dataSecurityAlertSender.sendMsgToManager(dingtalkManager, alert);
            if (alertRecord != null) {
                EdsInstanceProviderHolder<EdsEagleCloudConfigModel.Sase, EagleCloudModel.AlertRecord> alertRecordHolder = (EdsInstanceProviderHolder<EdsEagleCloudConfigModel.Sase, EagleCloudModel.AlertRecord>) holderBuilder.newHolder(
                        edsInstance.getId(), EdsAssetTypeEnum.EAGLECLOUD_SASE_DATA_SECURITY_ALERT_RECORD.name());
                if (alertRecordHolder != null) {
                    alertRecordHolder.importAsset(alertRecord);
                }
            }
        } catch (Exception e) {
            log.error("Data security alert send failed: {}", e.getMessage());
        }
    }

}
