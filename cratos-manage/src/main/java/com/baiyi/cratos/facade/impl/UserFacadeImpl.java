package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.annotation.PostImportProcessor;
import com.baiyi.cratos.annotation.PageQueryByTag;
import com.baiyi.cratos.annotation.SetSessionUserToParam;
import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.exception.UserException;
import com.baiyi.cratos.common.util.*;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessCredential;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.domain.param.http.user.UserExtParam;
import com.baiyi.cratos.domain.param.http.user.UserParam;
import com.baiyi.cratos.domain.view.credential.CredentialVO;
import com.baiyi.cratos.domain.view.user.UserVO;
import com.baiyi.cratos.facade.CredentialFacade;
import com.baiyi.cratos.facade.UserFacade;
import com.baiyi.cratos.service.BusinessCredentialService;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.TagService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.wrapper.CredentialWrapper;
import com.baiyi.cratos.wrapper.UserWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
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
    private final BusinessCredentialService businessCredentialService;
    private final CredentialService credentialService;
    private final TagService tagService;

    private final static Long NEW_PASSWORD_VALIDITY_PERIOD_DAYS = 90L;

    @Override
    @PageQueryByTag(typeOf = BusinessTypeEnum.USER)
    public DataTable<UserVO.User> queryUserPage(UserParam.UserPageQuery pageQuery) {
        DataTable<User> table = userService.queryUserPage(pageQuery.toParam());
        return userWrapper.wrapToTarget(table);
    }

    @SuppressWarnings("unchecked")
    @Override
    public DataTable<UserVO.User> queryCommandExecUserPage(UserParam.CommandExecUserPageQuery pageQuery) {
        Tag tag = tagService.getByTagKey(SysTagKeys.COMMAND_EXEC_APPROVER.getKey());
        if (Objects.isNull(tag)) {
            return DataTable.NO_DATA;
        }
        BusinessTagParam.QueryByTag queryByTag = BusinessTagParam.QueryByTag.builder()
                .tagId(tag.getId())
                .build();
        UserParam.UserPageQuery query = UserParam.UserPageQuery.builder()
                .page(pageQuery.getPage())
                .length(pageQuery.getLength())
                .queryName(pageQuery.getQueryName())
                .queryByTag(queryByTag)
                .build();
        return ((UserFacade) AopContext.currentProxy()).queryUserPage(query);
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
    @PostImportProcessor(ofType = BusinessTypeEnum.USER)
    public User addUser(UserParam.AddUser addUser) {
        User user = addUser.toTarget();
        if (!StringUtils.hasText(user.getUuid())) {
            user.setUuid(IdentityUtils.randomUUID());
        }
        userService.add(user);
        // 从DingTalk导入的用户，需要创举LDAP用户
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
                .expiredTime(ExpiredUtils.generateExpirationTime(NEW_PASSWORD_VALIDITY_PERIOD_DAYS, TimeUnit.DAYS))
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
    public void updateMyLanguage(UserParam.UpdateMyLanguage updateMyLanguage) {
        User user = userService.getByUsername(SessionUtils.getUsername());
        user.setLang(updateMyLanguage.getLang());
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
        UserParam.QuerySshKey querySshKey = UserParam.QuerySshKey.builder()
                .username(SecurityContextHolder.getContext()
                                  .getAuthentication()
                                  .getName())
                .build();
        return querySshKey(querySshKey);
    }

    @Override
    public List<CredentialVO.Credential> querySshKey(UserParam.QuerySshKey querySshKey) {
        return getUserPubKeyCredentials(querySshKey.getUsername()).stream()
                .map(credentialWrapper::wrapToTarget)
                .toList();
    }

    @Override
    @SetSessionUserToParam(desc = "set Author")
    public void addMySshKey(UserParam.AddMySshKey addMySshKey) {
        this.addSshKey(UserParam.AddSshKey.builder()
                               .username(addMySshKey.getUsername())
                               .pubKey(addMySshKey.getPubKey())
                               .build());
    }

    @Override
    public void addSshKey(UserParam.AddSshKey addSshKey) {
        if (!StringUtils.hasText(addSshKey.getUsername())) {
            return;
        }
        String pubKey = addSshKey.getPubKey();
        User user = userService.getByUsername(addSshKey.getUsername());
        if (user == null) {
            return;
        }
        Credential credential = Credential.builder()
                .title(SshKeyUtils.getTitle(pubKey))
                .username(addSshKey.getUsername())
                .credentialType(CredentialTypeEnum.SSH_USERNAME_WITH_PUBLIC_KEY.name())
                .credential(pubKey)
                .fingerprint(SshFingerprintUtils.calcFingerprint(null, pubKey))
                .privateCredential(true)
                .valid(true)
                .expiredTime(ExpiredUtils.generateExpirationTime(366L * 5, TimeUnit.DAYS))
                .build();
        credentialService.add(credential);
        BusinessCredential businessCredential = BusinessCredential.builder()
                .credentialId(credential.getId())
                .businessType(BusinessTypeEnum.USER.name())
                .businessId(user.getId())
                .build();
        businessCredentialService.add(businessCredential);
    }

    @Override
    public BaseValidService<?, ?> getValidService() {
        return userService;
    }

}