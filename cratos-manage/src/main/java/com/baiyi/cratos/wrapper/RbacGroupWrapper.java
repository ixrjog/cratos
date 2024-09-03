package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.RbacGroup;
import com.baiyi.cratos.domain.view.rbac.RbacGroupVO;
import com.baiyi.cratos.service.RbacGroupService;
import com.baiyi.cratos.service.RbacResourceService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBusinessWrapper;
import com.baiyi.cratos.wrapper.builder.ResourceCountBuilder;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.baiyi.cratos.domain.enums.BusinessTypeEnum.RBAC_RESOURCE;

/**
 * @Author baiyi
 * @Date 2024/1/17 18:02
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.RBAC_GROUP)
public class RbacGroupWrapper extends BaseDataTableConverter<RbacGroupVO.Group, RbacGroup> implements IBusinessWrapper<RbacGroupVO.HasRbacGroup, RbacGroupVO.Group> {

    private final RbacResourceService rbacResourceService;

    private final RbacGroupService rbacGroupService;

    @Override
    public void wrap(RbacGroupVO.Group vo) {
        Map<String, Integer> resourceCount = ResourceCountBuilder.newBuilder()
                .put(makeResourceCountForRbacResource(vo))
                .build();
        vo.setResourceCount(resourceCount);
    }

    private Map<String, Integer> makeResourceCountForRbacResource(RbacGroupVO.Group group) {
        Map<String, Integer> resourceCount = Maps.newHashMap();
        resourceCount.put(RBAC_RESOURCE.name(), rbacResourceService.selectCountByGroupId(group.getId()));
        return resourceCount;
    }

    @Override
    public void businessWrap(RbacGroupVO.HasRbacGroup group) {
        IdentityUtil.validIdentityRun(group.getRbacGroupId())
                .withTrue(() -> {
                    RbacGroup rbacGroup = rbacGroupService.getById(group.getRbacGroupId());
                    if (rbacGroup != null) {
                        RbacGroupVO.Group groupVO = this.convert(rbacGroup);
                        wrapFromProxy(groupVO);
                        group.setRbacGroup(groupVO);
                    }
                });
    }

}
