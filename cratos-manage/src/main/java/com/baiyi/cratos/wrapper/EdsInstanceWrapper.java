package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.common.configuration.CachingConfiguration;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.view.eds.EdsAssetTypeVO;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.eds.core.EdsInstanceProviderFactory;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsInstanceVersionProviderException;
import com.baiyi.cratos.eds.core.holder.EdsInstanceVersionProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceVersionProviderHolderBuilder;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/2/5 17:22
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EdsInstanceWrapper extends BaseDataTableConverter<EdsInstanceVO.EdsInstance, EdsInstance> implements IBaseWrapper<EdsInstanceVO.EdsInstance> {

    private final EdsConfigWrapper edsConfigWrapper;
    private final EdsInstanceVersionProviderHolderBuilder versionHolderBuilder;

    @Override
    @BusinessWrapper(ofTypes = {BusinessTypeEnum.BUSINESS_TAG})
    public void wrap(EdsInstanceVO.EdsInstance vo) {
        // Eds Instance Registered
        vo.setRegistered(vo.getConfigId() != null);
        // Wrap Eds Config
        edsConfigWrapper.wrap(vo);
        vo.setAssetTypes(EdsInstanceProviderFactory.getInstanceAssetTypes(vo.getEdsType()));
        List<EdsAssetTypeVO.Type> instanceAssetTypes = EdsInstanceProviderFactory.getInstanceAssetTypes(vo.getEdsType())
                .stream()
                .map(this::toType)
                .sorted(Comparator.comparingInt(EdsAssetTypeVO.Type::getSeq))
                .toList();
        vo.setInstanceAssetTypes(instanceAssetTypes);
        // Version
        String version = ((EdsInstanceWrapper) AopContext.currentProxy()).queryVersion(vo.getId());
        if (StringUtils.hasText(version)) {
            vo.setVersion(version);
        }
    }

    @Cacheable(cacheNames = CachingConfiguration.RepositoryName.SHORT_TERM, key = "'EDS:INSTANCE:VERSION:ID:' + #instanceId", unless = "#result == null")
    public String queryVersion(int instanceId) {
        try {
            EdsInstanceVersionProviderHolder<?> holder = versionHolderBuilder.newHolder(instanceId);
            return holder.version();
        } catch (EdsInstanceVersionProviderException ignored) {
            return "";
        }
    }

    private EdsAssetTypeVO.Type toType(String name) {
        EdsAssetTypeEnum type = EdsAssetTypeEnum.valueOf(name);
        return EdsAssetTypeVO.Type.builder()
                .type(type.name())
                .displayName(type.getDisplayName())
                .seq(type.getSeq())
                .build();
    }

}
