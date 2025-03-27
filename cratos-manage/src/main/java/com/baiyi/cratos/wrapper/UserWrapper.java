package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.annotation.Sensitive;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.view.user.UserVO;
import com.baiyi.cratos.service.RbacUserRoleService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import com.baiyi.cratos.wrapper.builder.ResourceCountBuilder;
import com.baiyi.cratos.wrapper.util.UserAvatarUtils;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.baiyi.cratos.domain.enums.BusinessTypeEnum.RBAC_USER_ROLE;

/**
 * @Author baiyi
 * @Date 2024/1/10 10:29
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserWrapper extends BaseDataTableConverter<UserVO.User, User> implements IBaseWrapper<UserVO.User> {

    private final RbacUserRoleService rbacUserRoleService;
    private final UserAvatarUtils userAvatarUtils;

    @Override
    @Sensitive
    @BusinessWrapper(ofTypes = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC, BusinessTypeEnum.RBAC_ROLE})
    public void wrap(UserVO.User vo) {
        Map<String, Integer> resourceCount = ResourceCountBuilder.newBuilder()
                .put(buildRbacUserRoleResourceCount(vo))
                .build();
        vo.setResourceCount(resourceCount);
        // 头像
        //vo.setAvatar(userAvatarUtils.queryUserAvatar(vo.getUsername()));
    }

    private Map<String, Integer> buildRbacUserRoleResourceCount(UserVO.User user) {
        Map<String, Integer> resourceCount = Maps.newHashMap();
        resourceCount.put(RBAC_USER_ROLE.name(), rbacUserRoleService.selectCountByUsername(user.getUsername()));
        return resourceCount;
    }

}
