package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.generator.RbacResource;
import com.baiyi.cratos.domain.view.user.FrontVO;
import com.baiyi.cratos.facade.RbacFacade;
import com.baiyi.cratos.facade.UiFacade;
import com.baiyi.cratos.service.RbacResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/14 17:38
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UiFacadeImpl implements UiFacade {

    private final RbacResourceService rbacResourceService;
    private final RbacFacade rbacFacade;

    @Override
    public FrontVO.UIPoint getUIPoint() {
        String username = SessionUtils.getUsername();
        if (!StringUtils.hasText(username)) {
            return FrontVO.UIPoint.NO_DATA;
        }
        List<RbacResource> resources = rbacResourceService.queryAllUiPoints();
        Map<String, Boolean> pointMap = resources.stream()
                .collect(Collectors.toMap(RbacResource::getResourceName,
                        e -> rbacFacade.hasResourceAccessPermissionsForUsername(username, e.getResourceName())));
        return FrontVO.UIPoint.builder()
                .pointMap(pointMap)
                .build();
    }

}
