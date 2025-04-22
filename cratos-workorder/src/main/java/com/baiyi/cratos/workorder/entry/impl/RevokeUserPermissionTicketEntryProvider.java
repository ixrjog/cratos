package com.baiyi.cratos.workorder.entry.impl;

import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.business.BusinessParam;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import com.baiyi.cratos.domain.view.user.UserVO;
import com.baiyi.cratos.eds.core.facade.EdsIdentityFacade;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.RevokeUserPermissionTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.entry.TicketEntryProvider;
import com.baiyi.cratos.workorder.entry.TicketEntryProviderFactory;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/14 10:06
 * &#064;Version 1.0
 */
@Slf4j
@Component
@BusinessType(type = BusinessTypeEnum.USER)
@WorkOrderKey(key = WorkOrderKeys.REVOKE_USER_PERMISSION)
public class RevokeUserPermissionTicketEntryProvider extends BaseTicketEntryProvider<UserVO.User, WorkOrderTicketParam.AddRevokeUserPermissionTicketEntry> {

    private final BusinessTagFacade businessTagFacade;
    private final EdsIdentityFacade edsIdentityFacade;
    private final UserService userService;

    public RevokeUserPermissionTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                   WorkOrderTicketService workOrderTicketService,
                                                   WorkOrderService workOrderService,
                                                   BusinessTagFacade businessTagFacade,
                                                   EdsIdentityFacade edsIdentityFacade, UserService userService) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.businessTagFacade = businessTagFacade;
        this.edsIdentityFacade = edsIdentityFacade;
        this.userService = userService;
    }

    private static final String TABLE_TITLE = """
            | Username | Name | DisplayName | Email | Tags |
            | --- | --- | --- | --- | --- |
            """;

    private static final String ROW_TPL = "| {} | {} | {} | {} | {} |";

    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                UserVO.User detail) throws WorkOrderTicketException {
        User user = userService.getByUsername(detail.getUsername());
        if (user != null && user.getValid()) {
            user.setValid(false);
            userService.updateByPrimaryKey(user);
        }
    }

    @Override
    protected WorkOrderTicketEntry paramToEntry(
            WorkOrderTicketParam.AddRevokeUserPermissionTicketEntry addRevokeUserTicketEntry) {
        return RevokeUserPermissionTicketEntryBuilder.newBuilder()
                .withParam(addRevokeUserTicketEntry)
                .buildEntry();
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return TABLE_TITLE;
    }

    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        UserVO.User user = loadAs(entry);
        BusinessParam.GetByBusiness getByBusiness = BusinessParam.GetByBusiness.builder()
                .businessType(BusinessTypeEnum.USER.name())
                .businessId(user.getId())
                .build();
        List<BusinessTagVO.BusinessTag> businessTags = businessTagFacade.getBusinessTagByBusiness(getByBusiness);
        String tags = CollectionUtils.isEmpty(businessTags) ? "-" : businessTags.stream()
                .map(e -> e.getTag()
                        .getTagKey())
                .collect(Collectors.joining(","));
        return StringFormatter.arrayFormat(ROW_TPL, user.getUsername(), user.getName(), user.getDisplayName(),
                user.getEmail(), tags);
    }

    @Override
    public TicketEntryModel.EntryDesc getEntryDesc(WorkOrderTicketEntry entry) {
        return TicketEntryModel.EntryDesc.builder()
                .name(entry.getName())
                .desc("User")
                .build();
    }

    @Override
    public WorkOrderTicketEntry addEntry(WorkOrderTicketParam.AddRevokeUserPermissionTicketEntry param) {
        boolean userEntryExists = queryTicketEntries(param.getTicketId()).stream()
                .anyMatch(entry -> BusinessTypeEnum.USER.name()
                        .equals(entry.getBusinessType()));
        if (userEntryExists) {
            WorkOrderTicketException.runtime("Only one user information can be inserted.");
        }
        WorkOrderTicketEntry entry = super.addEntry(param);
        addUserAccountAssets(param);
        return entry;
    }

    @SuppressWarnings("unchecked")
    private void addUserAccountAssets(WorkOrderTicketParam.AddRevokeUserPermissionTicketEntry param) {
        TicketEntryProvider<EdsAssetVO.Asset, WorkOrderTicketParam.AddRevokeUserEdsAccountPermissionTicketEntry> revokeUserEdsAccountPermissionTicketEntryProvider = (TicketEntryProvider<EdsAssetVO.Asset, WorkOrderTicketParam.AddRevokeUserEdsAccountPermissionTicketEntry>) TicketEntryProviderFactory.getProvider(
                getKey(), BusinessTypeEnum.EDS_ASSET.name());
        List<WorkOrderTicketParam.AddRevokeUserEdsAccountPermissionTicketEntry> allAccounts = Stream.of(
                        getUserLdapAccounts(param), getUserCloudAccounts(param), getUserGitLabAccounts(param),
                        getUserMailAccounts(param))
                .flatMap(List::stream)
                .toList();
        if (!CollectionUtils.isEmpty(allAccounts)) {
            allAccounts.forEach(revokeUserEdsAccountPermissionTicketEntryProvider::addEntry);
        }
    }

    @SuppressWarnings("unchecked")
    private List<WorkOrderTicketParam.AddRevokeUserEdsAccountPermissionTicketEntry> getUserLdapAccounts(
            WorkOrderTicketParam.AddRevokeUserPermissionTicketEntry param) {
        EdsIdentityParam.QueryLdapIdentityDetails queryLdapIdentityDetails = EdsIdentityParam.QueryLdapIdentityDetails.builder()
                .username(param.getDetail()
                        .getUsername())
                .build();
        EdsIdentityVO.LdapIdentityDetails ldapIdentityDetails = edsIdentityFacade.queryLdapIdentityDetails(
                queryLdapIdentityDetails);
        if (CollectionUtils.isEmpty(ldapIdentityDetails.getLdapIdentities())) {
            return List.of();
        }
        return (List<WorkOrderTicketParam.AddRevokeUserEdsAccountPermissionTicketEntry>) ldapIdentityDetails.getLdapIdentities()
                .stream()
                .map(e -> WorkOrderTicketParam.AddRevokeUserEdsAccountPermissionTicketEntry.builder()
                        .ticketId(param.getTicketId())
                        .detail(e.getAccount())
                        .build())
                .toList();
    }

    @SuppressWarnings("unchecked")
    private List<WorkOrderTicketParam.AddRevokeUserEdsAccountPermissionTicketEntry> getUserCloudAccounts(
            WorkOrderTicketParam.AddRevokeUserPermissionTicketEntry param) {
        EdsIdentityParam.QueryCloudIdentityDetails queryCloudIdentityDetails = EdsIdentityParam.QueryCloudIdentityDetails.builder()
                .username(param.getDetail()
                        .getUsername())
                .build();
        EdsIdentityVO.CloudIdentityDetails cloudIdentityDetails = edsIdentityFacade.queryCloudIdentityDetails(
                queryCloudIdentityDetails);
        if (CollectionUtils.isEmpty(cloudIdentityDetails.getAccounts())) {
            return List.of();
        }
        return (List<WorkOrderTicketParam.AddRevokeUserEdsAccountPermissionTicketEntry>) cloudIdentityDetails.getAccounts()
                .values()
                .stream()
                .flatMap(List::stream)
                .map(e -> WorkOrderTicketParam.AddRevokeUserEdsAccountPermissionTicketEntry.builder()
                        .ticketId(param.getTicketId())
                        .detail(e.getAccount())
                        .build())
                .toList();
    }

    @SuppressWarnings("unchecked")
    private List<WorkOrderTicketParam.AddRevokeUserEdsAccountPermissionTicketEntry> getUserGitLabAccounts(
            WorkOrderTicketParam.AddRevokeUserPermissionTicketEntry param) {
        EdsIdentityParam.QueryGitLabIdentityDetails queryGitLabIdentityDetails = EdsIdentityParam.QueryGitLabIdentityDetails.builder()
                .username(param.getDetail()
                        .getUsername())
                .build();
        EdsIdentityVO.GitLabIdentityDetails gitLabIdentityDetails = edsIdentityFacade.queryGitLabIdentityDetails(
                queryGitLabIdentityDetails);
        if (CollectionUtils.isEmpty(gitLabIdentityDetails.getGitLabIdentities())) {
            return List.of();
        }
        return (List<WorkOrderTicketParam.AddRevokeUserEdsAccountPermissionTicketEntry>) gitLabIdentityDetails.getGitLabIdentities()
                .stream()
                .map(e -> WorkOrderTicketParam.AddRevokeUserEdsAccountPermissionTicketEntry.builder()
                        .ticketId(param.getTicketId())
                        .detail(e.getAccount())
                        .build())
                .toList();
    }

    @SuppressWarnings("unchecked")
    private List<WorkOrderTicketParam.AddRevokeUserEdsAccountPermissionTicketEntry> getUserMailAccounts(
            WorkOrderTicketParam.AddRevokeUserPermissionTicketEntry param) {
        EdsIdentityParam.QueryMailIdentityDetails queryMailIdentityDetails = EdsIdentityParam.QueryMailIdentityDetails.builder()
                .username(param.getDetail()
                        .getUsername())
                .build();
        EdsIdentityVO.MailIdentityDetails mailIdentityDetails = edsIdentityFacade.queryMailIdentityDetails(
                queryMailIdentityDetails);
        if (CollectionUtils.isEmpty(mailIdentityDetails.getAccounts())) {
            return List.of();
        }
        return (List<WorkOrderTicketParam.AddRevokeUserEdsAccountPermissionTicketEntry>) mailIdentityDetails.getAccounts()
                .values()
                .stream()
                .flatMap(List::stream)
                .map(e -> WorkOrderTicketParam.AddRevokeUserEdsAccountPermissionTicketEntry.builder()
                        .ticketId(param.getTicketId())
                        .detail(e.getAccount())
                        .build())
                .toList();
    }

}
