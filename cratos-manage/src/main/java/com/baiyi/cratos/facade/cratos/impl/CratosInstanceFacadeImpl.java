package com.baiyi.cratos.facade.cratos.impl;

import com.baiyi.cratos.annotation.PageQueryByTag;
import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.exception.CratosInstanceException;
import com.baiyi.cratos.configuration.CratosInstanceStartConfig;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.enums.InstanceHealthStatus;
import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.domain.generator.CratosInstance;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.param.http.cratos.CratosInstanceParam;
import com.baiyi.cratos.domain.view.cratos.CratosInstanceVO;
import com.baiyi.cratos.facade.cratos.CratosInstanceFacade;
import com.baiyi.cratos.service.BusinessTagService;
import com.baiyi.cratos.service.CratosInstanceService;
import com.baiyi.cratos.service.TagService;
import com.baiyi.cratos.wrapper.CratosInstanceWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

import static com.baiyi.cratos.domain.constant.Global.ENV_PROD;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/7 10:51
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class CratosInstanceFacadeImpl implements CratosInstanceFacade {

    private final CratosInstanceService cratosInstanceService;
    private final CratosInstanceWrapper cratosInstanceWrapper;
    private final TagService tagService;
    private final BusinessTagService businessTagService;

    @Override
    @PageQueryByTag(typeOf = BusinessTypeEnum.CRATOS_INSTANCE)
    public DataTable<CratosInstanceVO.RegisteredInstance> queryRegisteredInstancePage(
            CratosInstanceParam.RegisteredInstancePageQuery pageQuery) {
        DataTable<CratosInstance> table = cratosInstanceService.queryCratosInstancePage(pageQuery.toParam());
        return cratosInstanceWrapper.wrapToTarget(table);
    }

    @Override
    public CratosInstanceVO.Health checkHealth() {
        if (CratosInstanceStartConfig.INET_ADDRESS == null) {
            return CratosInstanceVO.Health.of(InstanceHealthStatus.ERROR);
        }
        CratosInstance instance = cratosInstanceService.getByHostIp(
                CratosInstanceStartConfig.INET_ADDRESS.getHostAddress());
        if (instance == null || !instance.getValid()) {
            return CratosInstanceVO.Health.of(InstanceHealthStatus.ERROR);
        }
        return CratosInstanceVO.Health.of(InstanceHealthStatus.OK);
    }

    @Override
    public void setValidById(int id) {
        CratosInstance cratosInstance = cratosInstanceService.getById(id);
        if (Objects.isNull(cratosInstance)) {
            return;
        }
        // 保证生产环境有一个有效实例
        checkInstanceValid(cratosInstance);
        cratosInstanceService.updateValidById(id);
    }

    @Override
    public void deleteById(int id) {
        cratosInstanceService.deleteById(id);
    }

    private void checkInstanceValid(CratosInstance cratosInstance) {
        if (!cratosInstance.getValid()) {
            return;
        }
        Tag envTag = tagService.getByTagKey(SysTagKeys.ENV);
        if (Objects.isNull(envTag)) {
            return;
        }
        BusinessTag uk = BusinessTag.builder()
                .businessType(BusinessTypeEnum.CRATOS_INSTANCE.name())
                .businessId(cratosInstance.getId())
                .tagId(envTag.getId())
                .build();
        BusinessTag businessTag = businessTagService.getByUniqueKey(uk);
        if (Objects.isNull(businessTag) || !ENV_PROD.equalsIgnoreCase(businessTag.getTagValue())) {
            return;
        }
        List<BusinessTag> businessTags = businessTagService.queryByBusinessTypeAndTagId(
                        BusinessTypeEnum.CRATOS_INSTANCE.name(), envTag.getId())
                .stream()
                .filter(e -> ENV_PROD.equalsIgnoreCase(e.getTagValue()))
                .toList();
        if (businessTags.size() <= 1) {
            CratosInstanceException.runtime("The production environment is not multi instance.");
        }
        long availableInstances = businessTags.stream()
                .map(e -> cratosInstanceService.getById(e.getBusinessId()))
                .filter(CratosInstance::getValid)
                .count();
        if (availableInstances <= 1) {
            CratosInstanceException.runtime("There are less than 2 available instances in the production environment.");
        }
    }

}
