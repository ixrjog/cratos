package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.domain.generator.RbacGroup;
import com.baiyi.cratos.domain.view.rbac.RbacGroupVO;
import com.baiyi.cratos.service.RbacResourceService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
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
public class RbacGroupWrapper extends BaseDataTableConverter<RbacGroupVO.Group, RbacGroup> implements IBaseWrapper<RbacGroupVO.Group> {

    private final RbacResourceService rbacResourceService;

    @Override
    public void wrap(RbacGroupVO.Group group) {
        Map<String, Integer> resourceCount = ResourceCountBuilder.newBuilder()
                .put(buildResourceCountForRbacResource(group))
                .build();
        group.setResourceCount(resourceCount);
    }

    private Map<String, Integer> buildResourceCountForRbacResource(RbacGroupVO.Group group) {
        Map<String, Integer> resourceCount = Maps.newHashMap();
        resourceCount.put(RBAC_RESOURCE.name(), rbacResourceService.selectCountByGroupId(group.getId()));
        return resourceCount;
    }

}
