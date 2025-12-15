package com.baiyi.cratos.workorder.entry.impl.permission;

import com.baiyi.cratos.common.util.MarkdownUtils;
import com.baiyi.cratos.common.util.PasswordGenerator;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.domain.view.user.UserVO;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.facade.EdsIdentityFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.ldap.model.LdapPerson;
import com.baiyi.cratos.eds.ldap.repo.LdapPersonRepo;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.ResetUserPasswordTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import com.baiyi.cratos.workorder.notice.ResetUserPasswordNoticeSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.baiyi.cratos.workorder.enums.TableHeaderConstants.USER_RESET_PASSWORD;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/23 11:01
 * &#064;Version 1.0
 */
@Slf4j
@Component
@BusinessType(type = BusinessTypeEnum.USER)
@WorkOrderKey(key = WorkOrderKeys.USER_RESET_PASSWORD)
public class UserResetPasswordTicketEntryProvider extends BaseTicketEntryProvider<UserVO.User, WorkOrderTicketParam.AddResetUserPasswordTicketEntry> {

    private final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;
    private final EdsInstanceService edsInstanceService;
    private final EdsIdentityFacade edsIdentityFacade;
    private final WorkOrderService workOrderService;
    private final UserService userService;
    private final ResetUserPasswordNoticeSender resetUserPasswordNoticeSender;
    private final LdapPersonRepo ldapPersonRepo;

    public UserResetPasswordTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                WorkOrderTicketService workOrderTicketService,
                                                WorkOrderService workOrderService,
                                                EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder,
                                                EdsInstanceService edsInstanceService,
                                                EdsIdentityFacade edsIdentityFacade, UserService userService,
                                                ResetUserPasswordNoticeSender resetUserPasswordNoticeSender,
                                                LdapPersonRepo ldapPersonRepo) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.edsInstanceProviderHolderBuilder = edsInstanceProviderHolderBuilder;
        this.edsInstanceService = edsInstanceService;
        this.edsIdentityFacade = edsIdentityFacade;
        this.workOrderService = workOrderService;
        this.userService = userService;
        this.resetUserPasswordNoticeSender = resetUserPasswordNoticeSender;
        this.ldapPersonRepo = ldapPersonRepo;
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return MarkdownUtils.createTableHeader(USER_RESET_PASSWORD);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                UserVO.User user) throws WorkOrderTicketException {
        List<EdsIdentityVO.LdapIdentity> ldapIdentities = queryAndVerifyLdapIdentity(user.getUsername());
        if (CollectionUtils.isEmpty(ldapIdentities)) {
            WorkOrderTicketException.runtime("No LDAP identities found for the user.");
        }
        String newPassword = PasswordGenerator.generatePassword();
        for (EdsIdentityVO.LdapIdentity ldapIdentity : ldapIdentities) {
            EdsInstanceProviderHolder<EdsConfigs.Ldap, LdapPerson.Person> holder = (EdsInstanceProviderHolder<EdsConfigs.Ldap, LdapPerson.Person>) edsInstanceProviderHolderBuilder.newHolder(
                    ldapIdentity.getInstance()
                            .getId(), EdsAssetTypeEnum.LDAP_PERSON.name());
            LdapPerson.Person person = LdapPerson.Person.builder()
                    .username(user.getUsername())
                    .userPassword(newPassword)
                    .build();
            try {
                ldapPersonRepo.update(holder.getInstance()
                        .getConfig(), person);
            } catch (Exception e) {
                WorkOrderTicketException.runtime("Ldap instance {} update user password failed: {}",
                        ldapIdentity.getInstance()
                                .getInstanceName(), e.getMessage());
            }
        }
        sendMsg(workOrderTicket, user.getUsername(), newPassword);
    }

    /**
     * 发送通知
     *
     * @param workOrderTicket
     * @param password
     */
    private void sendMsg(WorkOrderTicket workOrderTicket, String username, String password) {
        try {
            WorkOrder workOrder = workOrderService.getById(workOrderTicket.getWorkOrderId());
            resetUserPasswordNoticeSender.sendMsg(workOrder, workOrderTicket, username, password);
        } catch (Exception e) {
            throw new WorkOrderTicketException("Sending user notification failed err: {}", e.getMessage());
        }
    }

    @Override
    protected WorkOrderTicketEntry paramToEntry(WorkOrderTicketParam.AddResetUserPasswordTicketEntry param) {
        int userId = Optional.of(param)
                .map(WorkOrderTicketParam.AddResetUserPasswordTicketEntry::getDetail)
                .map(UserVO.User::getId)
                .orElseThrow(() -> new WorkOrderTicketException("User ID is null"));
        User user = userService.getById(userId);
        if (Objects.isNull(user)) {
            WorkOrderTicketException.runtime("User not found with ID: {}", userId);
        }
        queryAndVerifyLdapIdentity(user.getUsername());
        return ResetUserPasswordTicketEntryBuilder.newBuilder()
                .withUser(user)
                .withParam(param)
                .buildEntry();
    }

    private List<EdsIdentityVO.LdapIdentity> queryAndVerifyLdapIdentity(String username) {
        EdsIdentityParam.QueryLdapIdentityDetails queryLdapIdentityDetails = EdsIdentityParam.QueryLdapIdentityDetails.builder()
                .username(username)
                .build();
        EdsIdentityVO.LdapIdentityDetails ldapIdentityDetails = edsIdentityFacade.queryLdapIdentityDetails(
                queryLdapIdentityDetails);
        if (CollectionUtils.isEmpty(ldapIdentityDetails.getLdapIdentities())) {
            WorkOrderTicketException.runtime("No LDAP identities found for the user.");
        }
        return ldapIdentityDetails.getLdapIdentities();
    }

    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        UserVO.User user = loadAs(entry);
        EdsInstance instance = edsInstanceService.getById(entry.getInstanceId());
        return MarkdownUtils.createTableRow(user.getUsername(), user.getName(), user.getDisplayName(),
                user.getEmail());
    }

    @Override
    public TicketEntryModel.EntryDesc getEntryDesc(WorkOrderTicketEntry entry) {
        return TicketEntryModel.EntryDesc.builder()
                .name(entry.getName())
                .namespaces(entry.getNamespace())
                .desc("LDAP User")
                .build();
    }

}
