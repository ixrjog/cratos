package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.annotation.BindAssetsAfterImport;
import com.baiyi.cratos.annotation.PageQueryByTag;
import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.common.exception.UserException;
import com.baiyi.cratos.common.util.ExpiredUtil;
import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.user.UserExtParam;
import com.baiyi.cratos.domain.param.http.user.UserParam;
import com.baiyi.cratos.domain.view.credential.CredentialVO;
import com.baiyi.cratos.domain.view.user.UserVO;
import com.baiyi.cratos.facade.CredentialFacade;
import com.baiyi.cratos.facade.UserFacade;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.wrapper.CredentialWrapper;
import com.baiyi.cratos.wrapper.UserWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @Author baiyi
 * @Date 2024/1/10 10:15
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade {

    private final UserService userService;
    private final UserWrapper userWrapper;
    private final CredentialFacade credentialFacade;
    private final CredentialWrapper credentialWrapper;

    private final static Long NEW_PASSWORD_VALIDITY_PERIOD_DAYS = 90L;

    @Override
    @PageQueryByTag(typeOf = BusinessTypeEnum.USER)
    public DataTable<UserVO.User> queryUserPage(UserParam.UserPageQuery pageQuery) {
        DataTable<User> table = userService.queryUserPage(pageQuery.toParam());
        return userWrapper.wrapToTarget(table);
    }

    @Override
    @PageQueryByTag(typeOf = BusinessTypeEnum.USER)
    public DataTable<UserVO.User> queryExtUserPage(UserExtParam.UserExtPageQuery pageQuery) {
        DataTable<User> table = userService.queryExtUserPage(pageQuery.toParam());
        return userWrapper.wrapToTarget(table);
    }

    @Override
    public UserVO.User getUserByUsername(String username) {
        User user = Optional.ofNullable(userService.getByUsername(username))
                .orElseThrow(() -> new UserException("User {} does not exist.", username));
        return userWrapper.wrapToTarget(user);
    }

    @Override
    @BindAssetsAfterImport
    public User addUser(UserParam.AddUser addUser) {
        User user = addUser.toTarget();
        if (!StringUtils.hasText(user.getUuid())) {
            user.setUuid(IdentityUtil.randomUUID());
        }
        userService.add(user);
        return user;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void resetUserPassword(UserParam.ResetPassword resetPassword) {
        final String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        resetUserPassword(username, resetPassword);
    }

    @Override
    public void resetUserPassword(String username, UserParam.ResetPassword resetPassword) {
        User user = Optional.ofNullable(userService.getByUsername(username))
                .orElseThrow(() -> new UserException("User {} does not exist.", username));
        SimpleBusiness business = SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.USER.name())
                .businessId(user.getId())
                .build();
        List<Credential> credentials = credentialFacade.queryCredentialByBusiness(business)
                .stream()
                .filter(cred -> cred.getPrivateCredential() && cred.getValid() && CredentialTypeEnum.USERNAME_WITH_PASSWORD.name()
                        .equals(cred.getCredentialType()))
                .toList();
        // Revoke credentials
        if (!CollectionUtils.isEmpty(credentials)) {
            credentials.forEach(cred -> credentialFacade.revokeCredentialById(cred.getId()));
        }
        // 新增用户凭据
        Credential credential = Credential.builder()
                .title(user.getDisplayName())
                .username(username)
                .credentialType(CredentialTypeEnum.USERNAME_WITH_PASSWORD.name())
                .credential(resetPassword.getPassword())
                .privateCredential(true)
                .expiredTime(ExpiredUtil.generateExpirationTime(NEW_PASSWORD_VALIDITY_PERIOD_DAYS, TimeUnit.DAYS))
                .valid(true)
                .build();
        credentialFacade.createBusinessCredential(credential, business);
    }

    private List<Credential> getUserPubKeyCredentials(String username) {
        User user = Optional.ofNullable(userService.getByUsername(username))
                .orElseThrow(() -> new UserException("User {} does not exist.", username));
        SimpleBusiness business = SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.USER.name())
                .businessId(user.getId())
                .build();
        return credentialFacade.queryCredentialByBusiness(business)
                .stream()
                .filter(cred -> {
                    if (!cred.getPrivateCredential()) {
                        return false;
                    }
                    return CredentialTypeEnum.SSH_USERNAME_WITH_PUBLIC_KEY.name()
                            .equals(cred.getCredentialType());
                })
                .toList();
    }

    @Override
    public void updateUser(UserParam.UpdateUser updateUser) {
        User user = userService.getById(updateUser.getId());
        Optional.ofNullable(userService.getById(updateUser.getId()))
                .orElseThrow(() -> new UserException("User id={} does not exist.", updateUser.getId()));
        // 设置允许更新的属性
        user.setName(updateUser.getName());
        user.setDisplayName(updateUser.getDisplayName());
        user.setEmail(updateUser.getEmail());
        user.setComment(updateUser.getComment());
        user.setMobilePhone(updateUser.getMobilePhone());
        user.setValid(updateUser.getValid());
        user.setExpiredTime(updateUser.getExpiredTime());
        userService.updateByPrimaryKey(user);
    }

    @Override
    public void updateUser(UserParam.UpdateMy updateMy) {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        if (StringUtils.hasText(username)) {
            User user = Optional.ofNullable(userService.getByUsername(username))
                    .orElseThrow(() -> new UserException("User {} does not exist.", username));
            user.setName(updateMy.getName());
            user.setDisplayName(updateMy.getDisplayName());
            user.setEmail(updateMy.getEmail());
            user.setComment(updateMy.getComment());
            user.setMobilePhone(updateMy.getMobilePhone());
            userService.updateByPrimaryKey(user);
        }
    }

    @Override
    public List<CredentialVO.Credential> queryMySshKey() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return getUserPubKeyCredentials(username).stream()
                .map(credentialWrapper::wrapToTarget)
                .toList();
    }

    @Override
    public BaseValidService<?, ?> getValidService() {
        return userService;
    }

}