package com.baiyi.cratos.workorder.entry.impl.emergency;

import com.baiyi.cratos.common.util.MarkdownUtils;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.domain.view.application.ApplicationVO;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.ApplicationDeploymentRedeployTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.TableHeaderConstants;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.holder.ApplicationRedeployTokenHolder;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/15 11:03
 * &#064;Version 1.0
 */
@Component
@BusinessType(type = BusinessTypeEnum.APPLICATION)
@WorkOrderKey(key = WorkOrderKeys.APPLICATION_REDEPLOY)
public class ApplicationRedeployTicketEntryProvider extends BaseTicketEntryProvider<ApplicationVO.Application, WorkOrderTicketParam.AddApplicationRedeployTicketEntry> {

    private final ApplicationRedeployTokenHolder applicationRedeployTokenHolder;

    public ApplicationRedeployTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                  WorkOrderTicketService workOrderTicketService,
                                                  WorkOrderService workOrderService,
                                                  ApplicationRedeployTokenHolder applicationRedeployTokenHolder) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.applicationRedeployTokenHolder = applicationRedeployTokenHolder;
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return MarkdownUtils.generateMarkdownTableHeader(TableHeaderConstants.APPLICATION_REDEPLOY);
    }

    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        ApplicationVO.Application application = loadAs(entry);
        List<BusinessTagVO.BusinessTag> businessTags = Optional.ofNullable(application.getBusinessTags())
                .orElse(List.of());
        String tags = businessTags.stream()
                .map(tag -> StringFormatter.arrayFormat("{}:{}", tag.getTag()
                        .getTagKey(), tag.getTagValue()))
                .collect(Collectors.joining(","));
        return MarkdownUtils.generateMarkdownTableRow(application.getApplicationName(), tags);
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
        applicationRedeployTokenHolder.setToken(workOrderTicket.getUsername(), application.getApplicationName(),
                workOrderTicket);
    }

    @Override
    public WorkOrderTicketEntry paramToEntry(WorkOrderTicketParam.AddApplicationRedeployTicketEntry param) {
        return ApplicationDeploymentRedeployTicketEntryBuilder.newBuilder()
                .withParam(param)
                .buildEntry();
    }

}
