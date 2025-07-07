package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.annotation.PageQueryByTag;
import com.baiyi.cratos.common.enums.RenewalExtUserTypeEnum;
import com.baiyi.cratos.common.exception.UserException;
import com.baiyi.cratos.common.util.ExpiredUtil;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.SimpleCommited;
import com.baiyi.cratos.domain.annotation.Committing;
import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.generator.UserPermission;
import com.baiyi.cratos.domain.param.http.user.UserExtParam;
import com.baiyi.cratos.domain.view.user.UserVO;
import com.baiyi.cratos.facade.UserExtFacade;
import com.baiyi.cratos.facade.UserFacade;
import com.baiyi.cratos.service.TagService;
import com.baiyi.cratos.service.UserPermissionService;
import com.baiyi.cratos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/5 15:51
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class UserExtFacadeImpl implements UserExtFacade {

    private final UserFacade userFacade;
    private final TagService tagService;
    private final UserService userService;
    private final UserPermissionService userPermissionService;
    private static final String EXTERNAL_USER_TAG = "ExternalUser";

    @SuppressWarnings("unchecked")
    @Override
    @PageQueryByTag(typeOf = BusinessTypeEnum.USER)
    public DataTable<UserVO.User> queryExtUserPage(UserExtParam.UserExtPageQuery pageQuery) {
        Tag extUserTag = tagService.getByTagKey(EXTERNAL_USER_TAG);
        if (Objects.isNull(extUserTag)) {
            return DataTable.NO_DATA;
        }
        pageQuery.setExtUserTagId(extUserTag.getId());
        return userFacade.queryExtUserPage(pageQuery);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SimpleCommited renewalOfExtUser(UserExtParam.RenewalExtUser renewalExtUser) {
        RenewalExtUserTypeEnum renewalExtUserTypeEnum = RenewalExtUserTypeEnum.valueOf(renewalExtUser.getRenewalType());
        Date renewalTime = ExpiredUtil.generateExpirationTime(renewalExtUserTypeEnum.getDays(), TimeUnit.DAYS);
        User user = userService.getByUsername(renewalExtUser.getUsername());
        if (Objects.isNull(user)) {
            UserException.runtime("User does not exist.");
        }
        user.setValid(Global.VALID);
        user.setLocked(Global.UNLOCKED);
        user.setExpiredTime(renewalTime);
        userService.updateByPrimaryKey(user);
        // renewalOfAll
        if (Boolean.TRUE.equals(renewalExtUser.getRenewalOfAll())) {
            renewalOfAllPermissions(user, renewalTime);
        }
        // commit
        return ((UserExtFacadeImpl) AopContext.currentProxy()).renewalOfExtUser(user.getId(), renewalExtUser);
    }

    private void renewalOfAllPermissions(User user, Date renewalTime) {
        List<UserPermission> userPermissions = userPermissionService.queryByUsername(user.getUsername());
        if (CollectionUtils.isEmpty(userPermissions)) {
            return;
        }
        for (UserPermission userPermission : userPermissions) {
            boolean hasUpdate = false;
            if (Boolean.FALSE.equals(userPermission.getValid())) {
                userPermission.setValid(Global.VALID);
                hasUpdate = true;
            }
            if (userPermission.getExpiredTime() == null || userPermission.getExpiredTime()
                    .getTime() < renewalTime.getTime()) {
                hasUpdate = true;
                userPermission.setExpiredTime(renewalTime);
            }
            if (hasUpdate) {
                userPermissionService.updateByPrimaryKey(userPermission);
            }
        }
    }

    @Committing(typeOf = BusinessTypeEnum.USER, businessId = "#userId")
    public SimpleCommited renewalOfExtUser(int userId, UserExtParam.RenewalExtUser renewalExtUser) {
        return SimpleCommited.builder()
                .name(renewalExtUser.getUsername())
                .commitContent(StringFormatter.arrayFormat("Update: renewalType={}", renewalExtUser.getRenewalType()))
                .commitMessage(renewalExtUser.getCommit()
                        .getMessage())
                .build();
    }

}
