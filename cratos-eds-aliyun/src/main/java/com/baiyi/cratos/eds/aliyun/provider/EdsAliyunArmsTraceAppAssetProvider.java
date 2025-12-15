package com.baiyi.cratos.eds.aliyun.provider;

import com.baiyi.cratos.common.util.EnvUtils;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.Env;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.eds.aliyun.model.AliyunArms;
import com.baiyi.cratos.eds.aliyun.repo.AliyunArmsRepo;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.model.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.EnvFacade;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.baiyi.cratos.domain.constant.Global.APP_NAME;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.ALIYUN_ARMS_APP_HOME;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.ENV;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/1 上午10:56
 * &#064;Version 1.0
 */
@Slf4j
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_ARMS_TRACE_APPS)
public class EdsAliyunArmsTraceAppAssetProvider extends BaseEdsInstanceAssetProvider<EdsAliyunConfigModel.Aliyun, AliyunArms.TraceApps> {

    private final EnvFacade envFacade;

    public EdsAliyunArmsTraceAppAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                              CredentialService credentialService,
                                              ConfigCredTemplate configCredTemplate,
                                              EdsAssetIndexFacade edsAssetIndexFacade,
                                              UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                              EdsInstanceProviderHolderBuilder holderBuilder, EnvFacade envFacade) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
        this.envFacade = envFacade;
    }

    @Override
    protected List<AliyunArms.TraceApps> listEntities(
            ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance) throws EdsQueryEntitiesException {
        try {
            return AliyunArmsRepo.listTraceApps(instance.getEdsConfigModel())
                    .stream()
                    .map(e -> {
                        List<AliyunArms.Tags> tags = CollectionUtils.isEmpty(e.getTags()) ? List.of() :
                                e.getTags()
                                        .stream()
                                        .map(t -> AliyunArms.Tags.builder()
                                                .key(t.getKey())
                                                .value(t.getValue())
                                                .build())
                                        .toList();
                        return AliyunArms.TraceApps.builder()
                                .appId(e.getAppId())
                                .appName(e.getAppName())
                                .pid(e.getPid())
                                .regionId(e.getRegionId())
                                .createTime(e.getCreateTime())
                                .show(e.getShow())
                                .source(e.getSource())
                                .type(e.getType())
                                .labels(e.getLabels())
                                .resourceGroupId(e.getResourceGroupId())
                                .updateTime(e.getUpdateTime())
                                .userId(e.getUserId())
                                .tags(tags)
                                .build();
                    })
                    .toList();
        } catch (Exception e) {
            log.debug(e.getMessage());
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
                                  AliyunArms.TraceApps entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getAppId())
                .nameOf(entity.getAppName())
                .assetKeyOf(entity.getPid())
                .regionOf(entity.getRegionId())
                .createdTimeOf(entity.getCreateTime())
                .build();
    }

    @Override
    protected List<EdsAssetIndex> toIndexes(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
                                            EdsAsset edsAsset, AliyunArms.TraceApps entity) {
        String name = entity.getAppName();
        Map<String, Env> envMap = envFacade.getEnvMap();
        String env = EnvUtils.getEnvSuffix(envMap, name);
        if (StringUtils.hasText(env)) {
            List<EdsAssetIndex> indices = Lists.newArrayList();
            indices.add(createEdsAssetIndex(edsAsset, ENV, env));
            String appName = StringFormatter.eraseLastStr(name, "-" + env);
            indices.add(createEdsAssetIndex(edsAsset, APP_NAME, appName));
            Optional.of(instance)
                    .map(ExternalDataSourceInstance::getEdsConfigModel)
                    .map(EdsAliyunConfigModel.Aliyun::getArms)
                    .map(EdsAliyunConfigModel.ARMS::getAppOverview)
                    .ifPresent(home -> {
                        String appHome = StringFormatter.arrayFormat(home, entity.getRegionId(),
                                entity.getPid());
                        indices.add(createEdsAssetIndex(edsAsset, ALIYUN_ARMS_APP_HOME, appHome));
                    });
            return indices;
        }
        return List.of();
    }

}