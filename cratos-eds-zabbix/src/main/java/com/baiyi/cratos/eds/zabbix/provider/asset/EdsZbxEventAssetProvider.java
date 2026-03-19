package com.baiyi.cratos.eds.zabbix.provider.asset;

import com.baiyi.cratos.common.util.PasswordGenerator;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.eds.core.BaseEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.context.EdsAssetProviderContext;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.util.SreBridgeUtils;
import com.baiyi.cratos.eds.core.util.SreEventFormatter;
import com.baiyi.cratos.eds.zabbix.enums.SeverityType;
import com.baiyi.cratos.eds.zabbix.facade.ZbxFacade;
import com.baiyi.cratos.eds.zabbix.result.ZbxEventResult;
import com.baiyi.cratos.eds.zabbix.result.ZbxHostResult;
import com.baiyi.cratos.eds.zabbix.sender.AlertNotificationSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.baiyi.cratos.eds.core.util.SreEventFormatter.EVENT_ID;
import static java.util.Map.entry;

/**
 * 只处理问题事件
 * &#064;Author  baiyi
 * &#064;Date  2025/11/6 15:19
 * &#064;Version 1.0
 */
@Slf4j
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ZABBIX, assetTypeOf = EdsAssetTypeEnum.ZBX_EVENT)
public class EdsZbxEventAssetProvider extends BaseEdsAssetProvider<EdsConfigs.Zabbix, ZbxEventResult.Event> {

    private final AlertNotificationSender alertNotificationSender;

    public static final String OPERATOR = "Zabbix";

    public EdsZbxEventAssetProvider(EdsAssetProviderContext context, AlertNotificationSender alertNotificationSender) {
        super(context);
        this.alertNotificationSender = alertNotificationSender;
    }

    @Override
    protected List<ZbxEventResult.Event> listEntities(
            ExternalDataSourceInstance<EdsConfigs.Zabbix> instance) throws EdsQueryEntitiesException {
        try {
            return ZbxFacade.listEvent(instance.getConfig());
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Zabbix> instance,
                                         ZbxEventResult.Event entity) {
        String hostName = "Unknown Host";
        List<ZbxHostResult.Host> hosts = Optional.of(entity)
                .map(ZbxEventResult.Event::getHosts)
                .orElse(List.of());
        if (!CollectionUtils.isEmpty(hosts)) {
            hostName = hosts.getFirst()
                    .getHost();
        }
        //  pk-obs-middleware-prod-2 | WARNING | Linux: FS [/data/1]: Space is low (used > 80%, total 195.9GB)
        final String assetKey = StringFormatter.arrayFormat(
                "{} | {} | {}", hostName,
                SeverityType.getName(entity.getSeverity()), entity.getName()
        );
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getEventid())
                .assetKeyOf(assetKey)
                .nameOf(entity.getName())
                .build();
    }

    @Override
    protected void afterAssetCreated(EdsAsset asset) {
        // 发送告警通知
        alertNotificationSender.sendAlertNotice(asset);
        // SRE
        ZbxEventResult.Event event = assetLoadAs(asset.getOriginalModel());
        if (!CollectionUtils.isEmpty(event.getHosts())) {
            // 按主机发送事件
            event.getHosts()
                    .forEach(host -> {
                        try {
                            Map<String, String> targetContent = Map.ofEntries(
                                    entry("host", host.getHost()),
                                    entry("name", host.getName()),
                                    entry("hostid", host.getHostid())
                            );
                            Map<String, String> ext = Map.ofEntries(
                                    entry(EVENT_ID, PasswordGenerator.generateNo()), entry("opdata", event.getOpdata()),
                                    entry("zabbixEventId", event.getEventid())
                            );
                            SreBridgeUtils.publish(com.baiyi.cratos.domain.model.SreBridgeModel.Event.builder()
                                                           .operator(OPERATOR)
                                                           .action(SreEventFormatter.Action.TRIGGER_EVENT.getValue())
                                                           .description(event.getName())
                                                           .target(host.getName())
                                                           .targetContent(SreEventFormatter.mapToJson(targetContent))
                                                           .affection("")
                                                           .severity(SeverityType.getName(event.getSeverity()))
                                                           .status("executed")
                                                           .type(SreEventFormatter.Type.ALERT.getValue())
                                                           .ext(ext)
                                                           .build());
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    });
        }
    }

}