package com.baiyi.cratos.workorder.entry.impl.emergency;

import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.application.ApplicationVO;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.ApplicationDeploymentPodDeleteTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.holder.ApplicationDeletePodTokenHolder;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/9 16:39
 * &#064;Version 1.0
 */
@Component
@BusinessType(type = BusinessTypeEnum.APPLICATION)
@WorkOrderKey(key = WorkOrderKeys.APPLICATION_DELETE_POD)
public class ApplicationDeploymentPodDeleteTicketEntryProvider extends BaseTicketEntryProvider<ApplicationVO.Application, WorkOrderTicketParam.AddApplicationDeletePodTicketEntry> {

    private final ApplicationDeletePodTokenHolder applicationDeletePodTokenHolder;

    public ApplicationDeploymentPodDeleteTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                             WorkOrderTicketService workOrderTicketService,
                                                             WorkOrderService workOrderService,
                                                             ApplicationDeletePodTokenHolder applicationDeletePodTokenHolder) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.applicationDeletePodTokenHolder = applicationDeletePodTokenHolder;
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return """
                | Application Name | Tags |
                | --- | --- |
                """;
    }

    private static final String ROW_TPL = "| {} | {} |";

    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        ApplicationVO.Application application = loadAs(entry);
        List<BusinessTagVO.BusinessTag> businessTags = Optional.ofNullable(application.getBusinessTags())
                .orElse(List.of());
        String tags = businessTags.stream()
                .map(tag -> StringFormatter.arrayFormat("{}:{}", tag.getTag()
                        .getTagKey(), tag.getTagValue()))
                .collect(Collectors.joining(","));
        return StringFormatter.arrayFormat(ROW_TPL, application.getApplicationName(), tags);
    }

    @Override
    public TicketEntryModel.EntryDesc getEntryDesc(WorkOrderTicketEntry entry) {
        return TicketEntryModel.EntryDesc.builder()
                .name(entry.getName())
                .namespaces(entry.getNamespace())
                .desc("Application")
                .build();
    }

    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                ApplicationVO.Application application) throws WorkOrderTicketException {
        // 审批完成后2h内可以删除应用容器
        applicationDeletePodTokenHolder.setToken(workOrderTicket.getUsername(), application.getApplicationName(),
                workOrderTicket);
    }

    @Override
    public WorkOrderTicketEntry paramToEntry(WorkOrderTicketParam.AddApplicationDeletePodTicketEntry param) {
        return ApplicationDeploymentPodDeleteTicketEntryBuilder.newBuilder()
                .withParam(param)
                .buildEntry();
    }

}
