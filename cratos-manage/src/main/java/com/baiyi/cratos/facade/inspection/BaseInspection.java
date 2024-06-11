package com.baiyi.cratos.facade.inspection;

import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.eds.EdsInstanceHelper;
import com.baiyi.cratos.eds.core.config.EdsDingtalkConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkRobot;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkRobotService;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.service.NotificationTemplateService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/8 下午4:30
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
@Slf4j
public abstract class BaseInspection implements InspectionTask, InitializingBean {

    private final NotificationTemplateService notificationTemplateService;

    private final DingtalkRobotService dingtalkRobotService;

    protected final EdsInstanceHelper edsInstanceHelper;

    private final EdsConfigService edsConfigService;

    @Value("${cratos.language:en-us}")
    protected String language;

    @Value("${spring.config.activate.on-profile:dev}")
    private String onProfile;

    public BaseInspection(NotificationTemplateService notificationTemplateService,
                          DingtalkRobotService dingtalkRobotService, EdsInstanceHelper edsInstanceHelper,
                          EdsConfigService edsConfigService) {
        this.notificationTemplateService = notificationTemplateService;
        this.dingtalkRobotService = dingtalkRobotService;
        this.edsInstanceHelper = edsInstanceHelper;
        this.edsConfigService = edsConfigService;
    }

    public void inspectionTask() {
        send();
    }

    protected NotificationTemplate getNotificationTemplate(String notificationTemplateKey) {
        NotificationTemplate query = NotificationTemplate.builder()
                .notificationTemplateKey(notificationTemplateKey)
                .lang(language)
                .build();
        return notificationTemplateService.getByUniqueKey(query);
    }

    protected DingtalkRobot.Msg toRobotMsg(String robotMsg) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(robotMsg, DingtalkRobot.Msg.class);
    }

    abstract protected String getMsg() throws IOException;

    protected void send() {
        List<EdsInstance> edsInstanceList = edsInstanceHelper.queryValidEdsInstance(EdsInstanceTypeEnum.DINGTALK_ROBOT,
                "InspectionNotification");
        if (CollectionUtils.isEmpty(edsInstanceList)) {
            log.warn("No available robots to send inspection notifications.");
            return;
        }
        List<? extends EdsInstanceProviderHolder<EdsDingtalkConfigModel.Robot, DingtalkRobot.Msg>> holders = (List<? extends EdsInstanceProviderHolder<EdsDingtalkConfigModel.Robot, DingtalkRobot.Msg>>) edsInstanceHelper.buildHolder(
                edsInstanceList, EdsAssetTypeEnum.DINGTALK_ROBOT_MSG.name());
        holders.forEach(providerHolder -> {
            EdsConfig edsConfig = edsConfigService.getById(providerHolder.getInstance()
                    .getEdsInstance()
                    .getConfigId());
            EdsDingtalkConfigModel.Robot robot = providerHolder.getProvider()
                    .produceConfig(edsConfig);
            try {
                DingtalkRobot.Msg message = toRobotMsg(getMsg());
                if (onProfile.equals("dev")) {
                    System.out.println(message);
                } else {
                    dingtalkRobotService.send(robot.getToken(), message);
                    providerHolder.importAsset(message);
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        });
    }

    @Override
    public void afterPropertiesSet() {
        InspectionFactory.register(this);
    }

}
