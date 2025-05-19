package com.baiyi.cratos.workorder.entry.impl.aliyun;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ram.model.v20150501.CreateUserResponse;
import com.aliyuncs.ram.model.v20150501.GetUserResponse;
import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.util.PasswordGenerator;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.model.AliyunModel;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.eds.aliyun.repo.AliyunRamUserRepo;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.TagService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.CreateAliyunRamUserTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import com.baiyi.cratos.workorder.notice.CreateAliyunRamUserNoticeHelper;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static com.baiyi.cratos.eds.aliyun.repo.AliyunRamUserRepo.CREATE_LOGIN_PROFILE;
import static com.baiyi.cratos.eds.aliyun.repo.AliyunRamUserRepo.ENABLE_MFA;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/19 10:42
 * &#064;Version 1.0
 */
@Component
@BusinessType(type = BusinessTypeEnum.EDS_INSTANCE)
@WorkOrderKey(key = WorkOrderKeys.ALIYUN_RAM_USER_PERMISSION)
public class AliyunRamUserPermissionTicketEntryProvider extends BaseTicketEntryProvider<AliyunModel.AliyunAccount, WorkOrderTicketParam.AddCreateAliyunRamUserTicketEntry> {

    private final EdsInstanceService edsInstanceService;
    private final AliyunRamUserRepo aliyunRamUserRepo;
    private final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;
    private final BusinessTagFacade businessTagFacade;
    private final TagService tagService;
    private final UserService userService;
    private final WorkOrderService workOrderService;
    private final CreateAliyunRamUserNoticeHelper createAliyunRamUserNoticeHelper;

    private static final String ROW_TPL = "| {} | {} | {} |";

    public AliyunRamUserPermissionTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                      WorkOrderTicketService workOrderTicketService,
                                                      WorkOrderService workOrderService,
                                                      EdsInstanceService edsInstanceService,
                                                      AliyunRamUserRepo aliyunRamUserRepo,
                                                      EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder,
                                                      BusinessTagFacade businessTagFacade, TagService tagService,
                                                      UserService userService,
                                                      CreateAliyunRamUserNoticeHelper createAliyunRamUserNoticeHelper) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.edsInstanceService = edsInstanceService;
        this.aliyunRamUserRepo = aliyunRamUserRepo;
        this.edsInstanceProviderHolderBuilder = edsInstanceProviderHolderBuilder;
        this.businessTagFacade = businessTagFacade;
        this.tagService = tagService;
        this.userService = userService;
        this.workOrderService = workOrderService;
        this.createAliyunRamUserNoticeHelper = createAliyunRamUserNoticeHelper;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                AliyunModel.AliyunAccount aliyunAccount) throws WorkOrderTicketException {
        // 创建Aliyun RAM
        EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, GetUserResponse.User> holder = (EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, GetUserResponse.User>) edsInstanceProviderHolderBuilder.newHolder(
                entry.getInstanceId(), EdsAssetTypeEnum.ALIYUN_RAM_USER.name());
        EdsAliyunConfigModel.Aliyun aliyun = holder.getInstance()
                .getEdsConfigModel();
        String ramUsername = aliyunAccount.getRamUsername();
        String ramLoginUsername = aliyunAccount.getRamLoginUsername();
        String username = aliyunAccount.getUsername();
        final String password = PasswordGenerator.generatePassword();
        CreateUserResponse.User createUser = createRAMUser(aliyun, ramUsername, password);
        // 发送通知
        sendMsg(workOrderTicket, username, ramLoginUsername, password, aliyun.getRam()
                .toLoginUrl());
        // 导入资产
        GetUserResponse.User getUser = getRamUser(aliyun, ramUsername);
        EdsAsset asset = holder.importAsset(getUser);
        // 关联用户
        addUsernameTag(asset, username);
    }

    /**
     * 发送通知
     *
     * @param workOrderTicket
     * @param username
     * @param ramLoginUsername
     */
    private void sendMsg(WorkOrderTicket workOrderTicket, String username, String ramLoginUsername, String password,
                         String loginLink) {
        try {
            WorkOrder workOrder = workOrderService.getById(workOrderTicket.getWorkOrderId());
            User applicantUser = userService.getByUsername(username);
            createAliyunRamUserNoticeHelper.sendMsg(workOrder, workOrderTicket, ramLoginUsername, password, loginLink,
                    applicantUser);
        } catch (Exception e) {
            throw new WorkOrderTicketException("Sending user notification failed err: {}", e.getMessage());
        }
    }

    private GetUserResponse.User getRamUser(EdsAliyunConfigModel.Aliyun aliyun, String ramUsername) {
        try {
            return aliyunRamUserRepo.getUser(aliyun, ramUsername);
        } catch (ClientException clientException) {
            throw new WorkOrderTicketException("Failed to get Aliyun RAM user err: {}", clientException.getMessage());
        }
    }

    private CreateUserResponse.User createRAMUser(EdsAliyunConfigModel.Aliyun aliyun, String ramUsername,
                                                  String password) {
        try {
            return aliyunRamUserRepo.createUser(aliyun.getRegionId(), aliyun, ramUsername, password,
                    CREATE_LOGIN_PROFILE, ENABLE_MFA);
        } catch (ClientException clientException) {
            throw new WorkOrderTicketException("Failed to create Aliyun RAM user err: {}",
                    clientException.getMessage());
        }
    }

    private void addUsernameTag(EdsAsset asset, String username) {
        // 标签
        Tag usernameTag = tagService.getByTagKey(SysTagKeys.USERNAME);
        if (Objects.isNull(usernameTag)) {
            return;
        }
        BusinessTagParam.SaveBusinessTag saveBusinessTag = BusinessTagParam.SaveBusinessTag.builder()
                .tagId(usernameTag.getId())
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .businessId(asset.getId())
                .tagValue(username)
                .build();
        businessTagFacade.saveBusinessTag(saveBusinessTag);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected WorkOrderTicketEntry paramToEntry(WorkOrderTicketParam.AddCreateAliyunRamUserTicketEntry param) {
       int instanceId = Optional.of(param)
                .map(WorkOrderTicketParam.AddCreateAliyunRamUserTicketEntry::getDetail)
                .map(AliyunModel.AliyunAccount::getEdsInstance)
                .map(EdsInstanceVO.EdsInstance::getId)
               .orElseThrow(()-> new WorkOrderTicketException("Eds instanceId is null"));
        EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, GetUserResponse.User> holder = (EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, GetUserResponse.User>) edsInstanceProviderHolderBuilder.newHolder(
                param.getDetail().getEdsInstance().getId(), EdsAssetTypeEnum.ALIYUN_RAM_USER.name());
        EdsAliyunConfigModel.Aliyun aliyun = holder.getInstance()
                .getEdsConfigModel();
        return CreateAliyunRamUserTicketEntryBuilder.newBuilder()
                .withParam(param)
                .withUsername(SessionUtils.getUsername())
                .withAliyun(aliyun)
                .buildEntry();
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return """
                | Aliyun Instance | RAM Username | Login Link |
                | --- | --- | --- |
                """;
    }

    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        AliyunModel.AliyunAccount aliyunAccount = loadAs(entry);
        EdsInstance instance = edsInstanceService.getById(entry.getInstanceId());
        return StringFormatter.arrayFormat(ROW_TPL, instance.getInstanceName(), aliyunAccount.getAccount(),
                aliyunAccount.getLoginLink());
    }

    @Override
    public TicketEntryModel.EntryDesc getEntryDesc(WorkOrderTicketEntry entry) {
        return TicketEntryModel.EntryDesc.builder()
                .name(entry.getName())
                .namespaces(entry.getNamespace())
                .desc("RAM User")
                .build();
    }

}
