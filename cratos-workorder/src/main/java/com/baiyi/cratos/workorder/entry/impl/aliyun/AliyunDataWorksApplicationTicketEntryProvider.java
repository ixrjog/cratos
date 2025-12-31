package com.baiyi.cratos.workorder.entry.impl.aliyun;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ram.model.v20150501.CreateAccessKeyResponse;
import com.aliyuncs.ram.model.v20150501.CreateUserResponse;
import com.aliyuncs.ram.model.v20150501.GetUserResponse;
import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.util.MarkdownUtils;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.model.AliyunDataWorksModel;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.eds.aliyun.repo.AliyunRamAccessKeyRepo;
import com.baiyi.cratos.eds.aliyun.repo.AliyunRamUserRepo;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
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
import com.baiyi.cratos.workorder.builder.entry.AliyunDataWorksInstanceTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.TableHeaderConstants;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import com.baiyi.cratos.workorder.notice.CreateDataWorkAKNoticeSender;
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
    private final UserService userService;
    private final CreateDataWorkAKNoticeSender createDataWorkAKNoticeSender;
    private final WorkOrderService workOrderService;

    public AliyunDataWorksApplicationTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                         WorkOrderTicketService workOrderTicketService,
                                                         WorkOrderService workOrderService,
                                                         EdsInstanceService edsInstanceService,
                                                         AliyunRamUserRepo aliyunRamUserRepo,
                                                         EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder,
                                                         BusinessTagFacade businessTagFacade, TagService tagService,
                                                         AliyunRamAccessKeyRepo aliyunRamAccessKeyRepo,
                                                         UserService userService,
                                                         CreateDataWorkAKNoticeSender createDataWorkAKNoticeSender) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.edsInstanceService = edsInstanceService;
        this.aliyunRamUserRepo = aliyunRamUserRepo;
        this.edsInstanceProviderHolderBuilder = edsInstanceProviderHolderBuilder;
        this.businessTagFacade = businessTagFacade;
        this.tagService = tagService;
        this.aliyunRamAccessKeyRepo = aliyunRamAccessKeyRepo;
        this.userService = userService;
        this.createDataWorkAKNoticeSender = createDataWorkAKNoticeSender;
        this.workOrderService = workOrderService;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                AliyunDataWorksModel.AliyunAccount aliyunAccount) throws WorkOrderTicketException {
        // 创建Aliyun RAM
        EdsInstanceProviderHolder<EdsConfigs.Aliyun, GetUserResponse.User> holder = (EdsInstanceProviderHolder<EdsConfigs.Aliyun, GetUserResponse.User>) edsInstanceProviderHolderBuilder.newHolder(
                entry.getInstanceId(), EdsAssetTypeEnum.ALIYUN_RAM_USER.name());
        EdsConfigs.Aliyun aliyun = holder.getInstance()
                .getConfig();
        String ramUsername = aliyunAccount.getAccount();
        String username = aliyunAccount.getUsername();
        User user = userService.getByUsername(username);
        CreateUserResponse.User createUser = createRAMUser(aliyun, user, ramUsername);
        CreateAccessKeyResponse.AccessKey accessKey = createAccessKey(aliyun, ramUsername);
        // 发送通知
        sendMsg(workOrderTicket, username, ramUsername, accessKey);
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
     * @param ramUsername
     * @param accessKey
     */
    private void sendMsg(WorkOrderTicket workOrderTicket, String username, String ramUsername,
                         CreateAccessKeyResponse.AccessKey accessKey) {
        try {
            WorkOrder workOrder = workOrderService.getById(workOrderTicket.getWorkOrderId());
            User applicantUser = userService.getByUsername(username);
            createDataWorkAKNoticeSender.sendMsg(workOrder, workOrderTicket, ramUsername, accessKey, applicantUser);
        } catch (Exception e) {
            throw new WorkOrderTicketException("Sending user notification failed err: {}", e.getMessage());
        }
    }

    private GetUserResponse.User getRamUser(EdsConfigs.Aliyun aliyun, String ramUsername) {
        try {
            return aliyunRamUserRepo.getUser(aliyun, ramUsername);
        } catch (ClientException clientException) {
            throw new WorkOrderTicketException("Failed to get Aliyun RAM user err: {}", clientException.getMessage());
        }
    }

    private CreateUserResponse.User createRAMUser(EdsConfigs.Aliyun aliyun, User user, String ramUsername) {
        try {
            return aliyunRamUserRepo.createUser(aliyun.getRegionId(), aliyun, user, ramUsername);
        } catch (ClientException clientException) {
            throw new WorkOrderTicketException(
                    "Failed to create Aliyun RAM user err: {}",
                                               clientException.getMessage()
            );
        }
    }

    private CreateAccessKeyResponse.AccessKey createAccessKey(EdsConfigs.Aliyun aliyun, String ramUsername) {
        try {
            return aliyunRamAccessKeyRepo.createAccessKey(aliyun, ramUsername);
        } catch (ClientException clientException) {
            throw new WorkOrderTicketException(
                    "Failed to create Aliyun RAM user accessKey err: {}",
                                               clientException.getMessage()
            );
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

    @Override
    protected WorkOrderTicketEntry paramToEntry(WorkOrderTicketParam.AddAliyunDataWorksInstanceTicketEntry param) {
        return AliyunDataWorksInstanceTicketEntryBuilder.newBuilder()
                .withParam(param)
                .withUsername(SessionUtils.getUsername())
                .buildEntry();
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return MarkdownUtils.createTableHeader(TableHeaderConstants.ALIYUN_DATAWORKS_AK);
    }

    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        AliyunDataWorksModel.AliyunAccount aliyunAccount = loadAs(entry);
        EdsInstance instance = edsInstanceService.getById(entry.getInstanceId());
        return MarkdownUtils.createTableRow(instance.getInstanceName(), aliyunAccount.getAccount());
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
