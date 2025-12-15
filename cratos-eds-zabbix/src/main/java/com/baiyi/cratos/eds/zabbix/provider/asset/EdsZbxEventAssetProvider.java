package com.baiyi.cratos.eds.zabbix.provider.asset;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.model.EdsZabbixConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.zabbix.enums.SeverityType;
import com.baiyi.cratos.eds.zabbix.facade.ZbxFacade;
import com.baiyi.cratos.eds.zabbix.result.ZbxEventResult;
import com.baiyi.cratos.eds.zabbix.result.ZbxHostResult;
import com.baiyi.cratos.eds.zabbix.sender.AlertNotificationSender;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * 只处理问题事件
 * &#064;Author  baiyi
 * &#064;Date  2025/11/6 15:19
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ZABBIX, assetTypeOf = EdsAssetTypeEnum.ZBX_EVENT)
public class EdsZbxEventAssetProvider extends BaseEdsInstanceAssetProvider<EdsZabbixConfigModel.Zabbix, ZbxEventResult.Event> {

    private final AlertNotificationSender alertNotificationSender;

    public EdsZbxEventAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                    CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                    EdsAssetIndexFacade edsAssetIndexFacade,
                                    UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                    EdsInstanceProviderHolderBuilder holderBuilder,
                                    AlertNotificationSender alertNotificationSender) {
        super(
                edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder
        );
        this.alertNotificationSender = alertNotificationSender;
    }

    @Override
    protected List<ZbxEventResult.Event> listEntities(
            ExternalDataSourceInstance<EdsZabbixConfigModel.Zabbix> instance) throws EdsQueryEntitiesException {
        try {
            return ZbxFacade.listEvent(instance.getConfig());
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsZabbixConfigModel.Zabbix> instance,
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
    }

}