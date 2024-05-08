package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.util.BeetlUtil;
import com.baiyi.cratos.common.util.ExpiredUtil;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.eds.EdsInstanceHelper;
import com.baiyi.cratos.eds.core.config.EdsDingtalkConfigModel;
import com.baiyi.cratos.eds.core.delegate.EdsInstanceProviderDelegate;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkRobot;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkRobotService;
import com.baiyi.cratos.facade.InspectionNotificationFacade;
import com.baiyi.cratos.service.CertificateService;
import com.baiyi.cratos.service.DomainService;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.service.NotificationTemplateService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/8 上午10:05
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
@Slf4j
@Component
@RequiredArgsConstructor
public class InspectionNotificationFacadeImpl implements InspectionNotificationFacade {

    private final NotificationTemplateService notificationTemplateService;

    private final DomainService domainService;

    private final CertificateService certificateService;

    private final DingtalkRobotService dingtalkRobotService;

    private final EdsInstanceHelper edsInstanceHelper;

    private final EdsConfigService edsConfigService;

    @Value("${cratos.language:en-us}")
    private String language;

    private static final int DOMAIN_EXPIRY_DAYS = 60;

    private static final int CERTIFICATE_EXPIRY_DAYS = 30;

    public static final String DOMAIN_EXPIRATION_INSPECTION_NOTIFICATION = "DOMAIN_EXPIRATION_INSPECTION_NOTIFICATION";

    public static final String CERTIFICATE_EXPIRATION_INSPECTION_NOTIFICATION = "CERTIFICATE_EXPIRATION_INSPECTION_NOTIFICATION";

    @Override
    public void domainInspectionTask() {
        NotificationTemplate query = NotificationTemplate.builder()
                .notificationTemplateKey(DOMAIN_EXPIRATION_INSPECTION_NOTIFICATION)
                .lang(language)
                .build();
        NotificationTemplate notificationTemplate = notificationTemplateService.getByUniqueKey(query);
        Date expiry = ExpiredUtil.generateExpirationTime(DOMAIN_EXPIRY_DAYS, TimeUnit.DAYS);
        List<Domain> domainList = domainService.queryByLessThanExpiry(expiry);

        List<EdsInstance> edsInstanceList = edsInstanceHelper.queryValidEdsInstance(EdsInstanceTypeEnum.DINGTALK_ROBOT,
                "InspectionNotification");
        if (CollectionUtils.isEmpty(edsInstanceList)) {
            log.warn("No available robots to send inspection notifications.");
            return;
        }
        List<? extends EdsInstanceProviderDelegate<EdsDingtalkConfigModel.Robot, DingtalkRobot.Msg>> edsInstanceProviderDelegates = (List<? extends EdsInstanceProviderDelegate<EdsDingtalkConfigModel.Robot, DingtalkRobot.Msg>>) edsInstanceHelper.buildDelegates(
                edsInstanceList, EdsAssetTypeEnum.DINGTALK_ROBOT_MSG.name());
        try {
            final String msg = BeetlUtil.renderTemplate(notificationTemplate.getContent(), SimpleMapBuilder.newBuilder()
                    .put("domains", domainList)
                    .put("expiryDays", DOMAIN_EXPIRY_DAYS)
                    .build());
            sendAndImport(edsInstanceProviderDelegates, msg);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private DingtalkRobot.Msg toRobotMsg(String robotMsg) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(robotMsg, DingtalkRobot.Msg.class);
    }

    @Override
    public void certificateInspectionTask() {
        NotificationTemplate query = NotificationTemplate.builder()
                .notificationTemplateKey(CERTIFICATE_EXPIRATION_INSPECTION_NOTIFICATION)
                .lang(language)
                .build();
        NotificationTemplate notificationTemplate = notificationTemplateService.getByUniqueKey(query);
        Date expiry = ExpiredUtil.generateExpirationTime(CERTIFICATE_EXPIRY_DAYS, TimeUnit.DAYS);
        List<Certificate> certificateList = certificateService.queryByLessThanExpiry(expiry);
        List<EdsInstance> edsInstanceList = edsInstanceHelper.queryValidEdsInstance(EdsInstanceTypeEnum.DINGTALK_ROBOT,
                "InspectionNotification");
        if (CollectionUtils.isEmpty(edsInstanceList)) {
            log.warn("No available robots to send inspection notifications.");
            return;
        }
        List<? extends EdsInstanceProviderDelegate<EdsDingtalkConfigModel.Robot, DingtalkRobot.Msg>> edsInstanceProviderDelegates = (List<? extends EdsInstanceProviderDelegate<EdsDingtalkConfigModel.Robot, DingtalkRobot.Msg>>) edsInstanceHelper.buildDelegates(
                edsInstanceList, EdsAssetTypeEnum.DINGTALK_ROBOT_MSG.name());
        try {
            final String msg = BeetlUtil.renderTemplate(notificationTemplate.getContent(), SimpleMapBuilder.newBuilder()
                    .put("certificates", certificateList)
                    .put("expiryDays", CERTIFICATE_EXPIRY_DAYS)
                    .build());

            sendAndImport(edsInstanceProviderDelegates, msg);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void sendAndImport(
            List<? extends EdsInstanceProviderDelegate<EdsDingtalkConfigModel.Robot, DingtalkRobot.Msg>> edsInstanceProviderDelegates,
            String msg) {
        edsInstanceProviderDelegates.forEach(edsInstanceProviderDelegate -> {
            EdsConfig edsConfig = edsConfigService.getById(edsInstanceProviderDelegate.getInstance()
                    .getEdsInstance()
                    .getConfigId());
            EdsDingtalkConfigModel.Robot robot = edsInstanceProviderDelegate.getProvider()
                    .produceConfig(edsConfig);
            DingtalkRobot.Msg message = toRobotMsg(msg);
            dingtalkRobotService.send(robot.getToken(), message);
            edsInstanceProviderDelegate.importAsset(message);
        });
    }

}
