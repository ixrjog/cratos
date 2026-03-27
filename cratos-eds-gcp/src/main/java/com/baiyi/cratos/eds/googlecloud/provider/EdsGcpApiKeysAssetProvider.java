package com.baiyi.cratos.eds.googlecloud.provider;

import com.baiyi.cratos.common.enums.SecurityLevel;
import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.eds.core.BaseEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.comparer.EdsAssetComparer;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.context.EdsAssetProviderContext;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsAssetConversionException;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.googlecloud.model.GcpApiKeysModel;
import com.baiyi.cratos.eds.googlecloud.repo.GcpApiKeysRepo;
import org.apache.commons.collections4.CollectionUtils;
import com.baiyi.cratos.service.TagService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/26 14:49
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.GCP, assetTypeOf = EdsAssetTypeEnum.GCP_API_KEYS)
public class EdsGcpApiKeysAssetProvider extends BaseEdsAssetProvider<EdsConfigs.Gcp, GcpApiKeysModel.Key> {

    private final GcpApiKeysRepo gcpApiKeysRepo;
    private final TagService tagService;
    private final BusinessTagFacade businessTagFacade;

    public EdsGcpApiKeysAssetProvider(EdsAssetProviderContext context, GcpApiKeysRepo gcpApiKeysRepo,
                                      TagService tagService, BusinessTagFacade businessTagFacade) {
        super(context);
        this.gcpApiKeysRepo = gcpApiKeysRepo;
        this.tagService = tagService;
        this.businessTagFacade = businessTagFacade;
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Gcp> instance,
                                         GcpApiKeysModel.Key entity) throws EdsAssetConversionException {
        return createAssetBuilder(instance, entity).assetIdOf(entity.getUid())
                .assetKeyOf(entity.getName())
                .nameOf(entity.getDisplayName())
                .validOf(entity.getDeleteTime() == null)
                .build();
    }

    @Override
    protected List<GcpApiKeysModel.Key> listEntities(
            ExternalDataSourceInstance<EdsConfigs.Gcp> instance) throws EdsQueryEntitiesException {
        try {
            return gcpApiKeysRepo.listApiKeys(instance.getConfig())
                    .stream()
                    .map(GcpApiKeysModel::toKey)
                    .toList();
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected boolean isAssetUnchanged(EdsAsset a1, EdsAsset a2) {
        return EdsAssetComparer.DIFFERENT;
    }

    @Override
    protected void processAssetTags(EdsAsset asset, ExternalDataSourceInstance<EdsConfigs.Gcp> instance,
                                    GcpApiKeysModel.Key entity, List<EdsAssetIndex> indices) {
        SecurityLevel level = evaluateSecurityLevel(entity.getRestrictions());
        BusinessTagParam.SaveBusinessTag saveBusinessTag = BusinessTagParam.SaveBusinessTag.builder()
                .tagId(tagService.getByTagKey(SysTagKeys.SECURITY_LEVEL.getKey()).getId())
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .businessId(asset.getId())
                .tagValue(level.name())
                .build();
        businessTagFacade.saveBusinessTag(saveBusinessTag);
    }

    /**
     * 评估 API Key 安全级别
     * <p>
     * HIGH:   无 API 限制 且 无应用限制（未设置任何保护）
     * MEDIUM: 仅设置了 API 限制 或 应用限制其中之一
     * LOW:    同时设置了 API 限制 和 应用限制（Android包名+指纹 / HTTP Referrer / IP白名单）
     */
    private SecurityLevel evaluateSecurityLevel(GcpApiKeysModel.Restrictions restrictions) {
        if (restrictions == null) {
            return SecurityLevel.HIGH;
        }
        boolean hasApiRestriction = !CollectionUtils.isEmpty(restrictions.getApiTargets());
        boolean hasAppRestriction = !CollectionUtils.isEmpty(restrictions.getAllowedApplications())
                || !CollectionUtils.isEmpty(restrictions.getClientRestrictions())
                || !CollectionUtils.isEmpty(restrictions.getAllowedIps());
        if (hasApiRestriction && hasAppRestriction) {
            return SecurityLevel.LOW;
        }
        if (hasApiRestriction || hasAppRestriction) {
            return SecurityLevel.MEDIUM;
        }
        return SecurityLevel.HIGH;
    }

}
