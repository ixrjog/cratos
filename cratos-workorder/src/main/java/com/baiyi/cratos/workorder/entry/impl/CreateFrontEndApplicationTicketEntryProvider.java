package com.baiyi.cratos.workorder.entry.impl;

import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.common.util.ValidationUtils;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.ApplicationModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.service.ApplicationService;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.CreateFrontEndApplicationTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Optional;

import static com.baiyi.cratos.common.enums.SysTagKeys.FRONT_END;
import static com.baiyi.cratos.common.enums.SysTagKeys.LEVEL;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.REPO_SSH_URL;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/16 15:55
 * &#064;Version 1.0
 */
@Component
@BusinessType(type = BusinessTypeEnum.APPLICATION)
@WorkOrderKey(key = WorkOrderKeys.APPLICATION_FRONTEND_CREATE)
public class CreateFrontEndApplicationTicketEntryProvider extends BaseTicketEntryProvider<ApplicationModel.CreateFrontEndApplication, WorkOrderTicketParam.AddCreateFrontEndApplicationTicketEntry> {

    private final ApplicationService applicationService;
    private final EdsAssetService edsAssetService;
    private final EdsAssetIndexService edsAssetIndexService;

    public CreateFrontEndApplicationTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                        WorkOrderTicketService workOrderTicketService,
                                                        WorkOrderService workOrderService,
                                                        ApplicationService applicationService,
                                                        EdsAssetService edsAssetService,
                                                        EdsAssetIndexService edsAssetIndexService) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.applicationService = applicationService;
        this.edsAssetService = edsAssetService;
        this.edsAssetIndexService = edsAssetIndexService;
    }

    private static final String ROW_TPL = "| {} | {} | {} | {} |";

    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                ApplicationModel.CreateFrontEndApplication createFrontEndApplication) throws WorkOrderTicketException {
        // TODO
    }

    @Override
    protected void verifyEntryParam(WorkOrderTicketParam.AddCreateFrontEndApplicationTicketEntry param,
                                    WorkOrderTicketEntry entry) {
        // 校验应用名称
        String applicationName = Optional.ofNullable(param)
                .map(WorkOrderTicketParam.AddCreateFrontEndApplicationTicketEntry::getDetail)
                .map(ApplicationModel.CreateFrontEndApplication::getApplicationName)
                .orElseThrow(() -> new WorkOrderTicketException("Application name cannot be empty."));
        if (!ValidationUtils.isApplicationName(applicationName)) {
            WorkOrderTicketException.runtime(
                    "Application name is not valid, it must start with a letter and can only contain lowercase letters, numbers, and hyphens.");
        }
        if (applicationService.getByName(applicationName) != null) {
            WorkOrderTicketException.runtime("Application name already exists.");
        }
        // 校验tags
        Map<String, String> tags = Optional.of(param)
                .map(WorkOrderTicketParam.AddCreateFrontEndApplicationTicketEntry::getDetail)
                .map(ApplicationModel.CreateFrontEndApplication::getTags)
                .orElseThrow(() -> new WorkOrderTicketException("Tags cannot be empty."));
        if (!tags.containsKey(LEVEL.getKey())) {
            WorkOrderTicketException.runtime("Level tag is required.");
        }
        // 校验仓库信息
        ApplicationModel.GitLabRepository repository = Optional.of(param)
                .map(WorkOrderTicketParam.AddCreateFrontEndApplicationTicketEntry::getDetail)
                .map(ApplicationModel.CreateFrontEndApplication::getRepository)
                .orElseThrow(() -> new WorkOrderTicketException("Repository information cannot be empty."));
        int assetId = Optional.ofNullable(repository.getAssetId())
                .orElseThrow(() -> new WorkOrderTicketException("Repository asset ID cannot be empty."));
        EdsAsset asset = edsAssetService.getById(assetId);
        if (asset == null) {
            WorkOrderTicketException.runtime("Repository asset does not exist.");
        }
        if (!EdsAssetTypeEnum.GITLAB_PROJECT.name()
                .equals(asset.getAssetType())) {
            WorkOrderTicketException.runtime("Repository asset type must be GitLab project.");
        }
        // 校验映射路径
        String mappingsPath = Optional.of(param)
                .map(WorkOrderTicketParam.AddCreateFrontEndApplicationTicketEntry::getDetail)
                .map(ApplicationModel.CreateFrontEndApplication::getMappingsPath)
                .filter(path -> !"/".equals(path))
                .orElse(null);
        if (mappingsPath != null && !ValidationUtils.isMappingsPath(mappingsPath)) {
            WorkOrderTicketException.runtime(
                    "Mappings path is not valid, it must start with a slash and can only contain lowercase letters, numbers, hyphens, underscores, and periods.");
        }
    }

    @Override
    protected WorkOrderTicketEntry paramToEntry(
            WorkOrderTicketParam.AddCreateFrontEndApplicationTicketEntry addCreateFrontEndApplicationTicketEntry) {
        int gitLabProjectAssetId = Optional.ofNullable(addCreateFrontEndApplicationTicketEntry)
                .map(WorkOrderTicketParam.AddCreateFrontEndApplicationTicketEntry::getDetail)
                .map(ApplicationModel.CreateFrontEndApplication::getRepository)
                .map(ApplicationModel.GitLabRepository::getAssetId)
                .orElseThrow(() -> new WorkOrderTicketException("Repository asset ID cannot be empty."));
        EdsAsset gitLabProjectAsset = edsAssetService.getById(gitLabProjectAssetId);
        EdsAssetIndex sshUrlIndex = edsAssetIndexService.getByAssetIdAndName(gitLabProjectAsset.getId(), REPO_SSH_URL);
        String sshUrl = Optional.ofNullable(sshUrlIndex)
                .map(EdsAssetIndex::getValue)
                .orElseThrow(() -> new WorkOrderTicketException("Repository SSH URL cannot be empty."));
        return CreateFrontEndApplicationTicketEntryBuilder.newBuilder()
                .withParam(addCreateFrontEndApplicationTicketEntry)
                .withGitLabProjectAsset(gitLabProjectAsset)
                .withSshUrl(sshUrl)
                .buildEntry();
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return """
                | Application Name | Type | Level | Repository SSH URL |
                | --- | --- | --- | --- |
                """;
    }

    /**
     * @param entry
     * @return
     */
    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        ApplicationModel.CreateFrontEndApplication createFrontEndApplication = loadAs(entry);
        String level = "--";
        if (!CollectionUtils.isEmpty(createFrontEndApplication.getTags())) {
            Map<String, String> tags = createFrontEndApplication.getTags();
            if (tags.containsKey(SysTagKeys.LEVEL.getKey())) {
                level = tags.get(SysTagKeys.LEVEL.getKey());
            }
        }
        String sshUrl = Optional.of(createFrontEndApplication)
                .map(ApplicationModel.CreateFrontEndApplication::getRepository)
                .map(ApplicationModel.GitLabRepository::getSshUrl)
                .orElse("--");
        return StringFormatter.arrayFormat(ROW_TPL, createFrontEndApplication.getApplicationName(), FRONT_END.getKey(),
                level, sshUrl);
    }

    @Override
    public TicketEntryModel.EntryDesc getEntryDesc(WorkOrderTicketEntry entry) {
        return TicketEntryModel.EntryDesc.builder()
                .name(entry.getName())
                .namespaces(entry.getNamespace())
                .desc("Application creation")
                .build();
    }

}
