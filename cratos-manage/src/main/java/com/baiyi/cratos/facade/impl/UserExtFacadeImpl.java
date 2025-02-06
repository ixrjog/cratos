package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.annotation.PageQueryByTag;
import com.baiyi.cratos.common.enums.RenewalExtUserTypeEnum;
import com.baiyi.cratos.common.exception.UserException;
import com.baiyi.cratos.common.util.ExpiredUtil;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.SimpleCommited;
import com.baiyi.cratos.domain.annotation.Committing;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.user.UserExtParam;
import com.baiyi.cratos.domain.view.user.UserVO;
import com.baiyi.cratos.facade.UserExtFacade;
import com.baiyi.cratos.facade.UserFacade;
import com.baiyi.cratos.service.TagService;
import com.baiyi.cratos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Component;

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
    public void renewalOfExtUser(UserExtParam.RenewalExtUser renewalExtUser) {
        RenewalExtUserTypeEnum renewalExtUserTypeEnum = RenewalExtUserTypeEnum.valueOf(renewalExtUser.getRenewalType());
        User user = userService.getByUsername(renewalExtUser.getUsername());
        if (Objects.isNull(user)) {
            UserException.runtime("User does not exist.");
        }
        user.setValid(true);
        user.setLocked(false);
        user.setExpiredTime(ExpiredUtil.generateExpirationTime(renewalExtUserTypeEnum.getDays(), TimeUnit.DAYS));
        userService.updateByPrimaryKey(user);
        ((UserExtFacadeImpl) AopContext.currentProxy()).renewalOfExtUser(user.getId(), renewalExtUser);
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
