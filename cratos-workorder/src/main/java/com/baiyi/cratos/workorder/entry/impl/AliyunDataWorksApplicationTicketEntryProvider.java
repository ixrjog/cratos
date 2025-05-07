package com.baiyi.cratos.workorder.entry.impl;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ram.model.v20150501.CreateAccessKeyResponse;
import com.aliyuncs.ram.model.v20150501.CreateUserResponse;
import com.aliyuncs.ram.model.v20150501.GetUserResponse;
import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.model.AliyunDataWorksModel;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.eds.aliyun.repo.AliyunRamAccessKeyRepo;
import com.baiyi.cratos.eds.aliyun.repo.AliyunRamUserRepo;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.facade.EdsDingtalkMessageFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.TagService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.AddAliyunDataWorksInstanceTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import com.baiyi.cratos.workorder.notice.CreateDataWorkAKNoticeHelper;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/6 11:33
 * &#064;Version 1.0
 */
@Component
@BusinessType(type = BusinessTypeEnum.EDS_INSTANCE)
@WorkOrderKey(key = WorkOrderKeys.ALIYUN_DATAWORKS_AK)
public class AliyunDataWorksApplicationTicketEntryProvider extends BaseTicketEntryProvider<AliyunDataWorksModel.AliyunAccount, WorkOrderTicketParam.AddAliyunDataWorksInstanceTicketEntry> {

    private final EdsInstanceService edsInstanceService;
    private final AliyunRamUserRepo aliyunRamUserRepo;
    private final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;
    private final BusinessTagFacade businessTagFacade;
    private final TagService tagService;
    private final AliyunRamAccessKeyRepo aliyunRamAccessKeyRepo;
    private final EdsDingtalkMessageFacade edsDingtalkMessageFacade;
    private final UserService userService;
    private final CreateDataWorkAKNoticeHelper createDataWorkAKNoticeHelper;

    public AliyunDataWorksApplicationTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                         WorkOrderTicketService workOrderTicketService,
                                                         WorkOrderService workOrderService,
                                                         EdsInstanceService edsInstanceService,
                                                         AliyunRamUserRepo aliyunRamUserRepo,
                                                         EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder,
                                                         BusinessTagFacade businessTagFacade, TagService tagService,
                                                         AliyunRamAccessKeyRepo aliyunRamAccessKeyRepo,
                                                         EdsDingtalkMessageFacade edsDingtalkMessageFacade,
                                                         UserService userService,
                                                         CreateDataWorkAKNoticeHelper createDataWorkAKNoticeHelper) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.edsInstanceService = edsInstanceService;
        this.aliyunRamUserRepo = aliyunRamUserRepo;
        this.edsInstanceProviderHolderBuilder = edsInstanceProviderHolderBuilder;
        this.businessTagFacade = businessTagFacade;
        this.tagService = tagService;
        this.aliyunRamAccessKeyRepo = aliyunRamAccessKeyRepo;
        this.edsDingtalkMessageFacade = edsDingtalkMessageFacade;
        this.userService = userService;
        this.createDataWorkAKNoticeHelper = createDataWorkAKNoticeHelper;
    }

    private static final String ROW_TPL = "| {} | {} |";

    @SuppressWarnings("unchecked")
    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                AliyunDataWorksModel.AliyunAccount aliyunAccount) throws WorkOrderTicketException {
        // 创建Aliyun RAM
        EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, GetUserResponse.User> holder = (EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, GetUserResponse.User>) edsInstanceProviderHolderBuilder.newHolder(
                entry.getInstanceId(), EdsAssetTypeEnum.ALIYUN_RAM_USER.name());
        EdsAliyunConfigModel.Aliyun aliyun = holder.getInstance()
                .getEdsConfigModel();
        String ramUsername = aliyunAccount.getAccount();
        String username = aliyunAccount.getUsername();
        CreateUserResponse.User createUser = createRAMUser(aliyun, ramUsername);
        CreateAccessKeyResponse.AccessKey accessKey = createAccessKey(aliyun, ramUsername);
        // TODO 发送通知

        // 导入资产
        GetUserResponse.User getUser = getRamUser(aliyun, ramUsername);
        EdsAsset asset = holder.importAsset(getUser);
        // 关联用户
        addUsernameTag(asset, username);
    }

    private void sendDingtalkMessage(String username) {

        //  void sendToDingtalkUser(User sendToUser, NotificationTemplate notificationTemplate, String msgText);
        User user = userService.getByUsername(username);
        // createDataWorkAKNoticeHelper.sendMsg();

    }

    private GetUserResponse.User getRamUser(EdsAliyunConfigModel.Aliyun aliyun, String ramUsername) {
        try {
            return aliyunRamUserRepo.getUser(aliyun, ramUsername);
        } catch (ClientException clientException) {
            throw new WorkOrderTicketException("Failed to get Aliyun RAM user err: {}", clientException.getMessage());
        }
    }

    private CreateUserResponse.User createRAMUser(EdsAliyunConfigModel.Aliyun aliyun, String ramUsername) {
        try {
            return aliyunRamUserRepo.createUser(aliyun.getRegionId(), aliyun, ramUsername);
        } catch (ClientException clientException) {
            throw new WorkOrderTicketException("Failed to create Aliyun RAM user err: {}",
                    clientException.getMessage());
        }
    }

    private CreateAccessKeyResponse.AccessKey createAccessKey(EdsAliyunConfigModel.Aliyun aliyun, String ramUsername) {
        try {
            return aliyunRamAccessKeyRepo.createAccessKey(aliyun, ramUsername);
        } catch (ClientException clientException) {
            throw new WorkOrderTicketException("Failed to create Aliyun RAM user accessKey err: {}",
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
                .businessType(getBusinessType())
                .businessId(asset.getId())
                .tagValue(username)
                .build();
        businessTagFacade.saveBusinessTag(saveBusinessTag);
    }

    @Override
    protected WorkOrderTicketEntry paramToEntry(WorkOrderTicketParam.AddAliyunDataWorksInstanceTicketEntry param) {
        return AddAliyunDataWorksInstanceTicketEntryBuilder.newBuilder()
                .withParam(param)
                .withUsername(SessionUtils.getUsername())
                .buildEntry();
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return """
                | Aliyun Instance | AK RAM User |
                | --- | --- |
                """;
    }

    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        AliyunDataWorksModel.AliyunAccount aliyunAccount = loadAs(entry);
        EdsInstance instance = edsInstanceService.getById(entry.getInstanceId());
        return StringFormatter.arrayFormat(ROW_TPL, instance.getInstanceName(), aliyunAccount.getAccount());
    }

    @Override
    public TicketEntryModel.EntryDesc getEntryDesc(WorkOrderTicketEntry entry) {
        return TicketEntryModel.EntryDesc.builder()
                .name(entry.getName())
                .namespaces(entry.getNamespace())
                .desc("AK RAM User")
                .build();
    }

}
