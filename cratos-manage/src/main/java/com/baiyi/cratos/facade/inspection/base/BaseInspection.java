package com.baiyi.cratos.facade.inspection.base;

import com.baiyi.cratos.common.enums.NotificationTemplateKeys;
import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.eds.core.EdsInstanceHelper;
import com.baiyi.cratos.eds.core.config.EdsDingtalkConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkRobotModel;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkService;
import com.baiyi.cratos.facade.inspection.InspectionFactory;
import com.baiyi.cratos.facade.inspection.InspectionTask;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.service.NotificationTemplateService;
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
    private final DingtalkService dingtalkService;
    protected final EdsInstanceHelper edsInstanceHelper;
    private final EdsConfigService edsConfigService;

    @Value("${cratos.language:en-us}")
    protected String language;

    @Value("${cratos.notification:NORMAL}")
    private String notification;

    public BaseInspection(NotificationTemplateService notificationTemplateService, DingtalkService dingtalkService,
                          EdsInstanceHelper edsInstanceHelper, EdsConfigService edsConfigService) {
        this.notificationTemplateService = notificationTemplateService;
        this.dingtalkService = dingtalkService;
        this.edsInstanceHelper = edsInstanceHelper;
        this.edsConfigService = edsConfigService;
    }

    public void inspectionTask() {
        send();
    }

    protected NotificationTemplate getNotificationTemplate(NotificationTemplateKeys key) {
        NotificationTemplate query = NotificationTemplate.builder()
                .notificationTemplateKey(key.name())
                .lang(language)
                .build();
        return notificationTemplateService.getByUniqueKey(query);
    }

    abstract protected String getMsg() throws IOException;

    protected void send() {
        List<EdsInstance> edsInstanceList = edsInstanceHelper.queryValidEdsInstance(
                EdsInstanceTypeEnum.DINGTALK_ROBOT,
                SysTagKeys.INSPECTION_NOTIFICATION.getKey()
        );
        if (CollectionUtils.isEmpty(edsInstanceList)) {
            log.warn("No available robots to send inspection notifications.");
            return;
        }
        List<? extends EdsInstanceProviderHolder<EdsDingtalkConfigModel.Robot, DingtalkRobotModel.Msg>> holders = (List<? extends EdsInstanceProviderHolder<EdsDingtalkConfigModel.Robot, DingtalkRobotModel.Msg>>) edsInstanceHelper.buildHolder(
                edsInstanceList, EdsAssetTypeEnum.DINGTALK_ROBOT_MSG.name());
        holders.forEach(providerHolder -> {
            EdsConfig edsConfig = edsConfigService.getById(providerHolder.getInstance()
                                                                   .getEdsInstance()
                                                                   .getConfigId());
            EdsDingtalkConfigModel.Robot robot = providerHolder.getProvider()
                    .configLoadAs(edsConfig);
            try {
                DingtalkRobotModel.Msg message = DingtalkRobotModel.loadAs(getMsg());
                if (notification.equals("LOCAL")) {
                    // 本地调试打印到控制台
                    System.out.println(message);
                } else {
                    dingtalkService.send(robot.getToken(), message);
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
