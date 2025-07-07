package com.baiyi.cratos.workorder.builder.entry;

import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.ApplicationModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/19 10:11
 * &#064;Version 1.0
 */
public class CreateFrontEndApplicationTicketEntryBuilder {

    private WorkOrderTicketParam.AddCreateFrontEndApplicationTicketEntry param;
    private EdsAsset gitLabProjectAsset;
    private String sshUrl;
    private Map<String, String> tags;

    public static CreateFrontEndApplicationTicketEntryBuilder newBuilder() {
        return new CreateFrontEndApplicationTicketEntryBuilder();
    }

    public CreateFrontEndApplicationTicketEntryBuilder withParam(
            WorkOrderTicketParam.AddCreateFrontEndApplicationTicketEntry param) {
        this.param = param;
        return this;
    }

    public CreateFrontEndApplicationTicketEntryBuilder withGitLabProjectAsset(EdsAsset gitLabProjectAsset) {
        this.gitLabProjectAsset = gitLabProjectAsset;
        return this;
    }

    public CreateFrontEndApplicationTicketEntryBuilder withSshUrl(String sshUrl) {
        this.sshUrl = sshUrl;
        return this;
    }

    public CreateFrontEndApplicationTicketEntryBuilder withTags(Map<String, String> tags) {
        this.tags = tags;
        return this;
    }

    public WorkOrderTicketEntry buildEntry() {
        ApplicationModel.CreateFrontEndApplication detail = param.getDetail();
        detail.getRepository()
                .setInstanceId(gitLabProjectAsset.getInstanceId());
        detail.getRepository()
                .setSshUrl(sshUrl);
        this.tags.putAll(detail.getTags());
        detail.setTags(this.tags);
        return WorkOrderTicketEntry.builder()
                .ticketId(param.getTicketId())
                .name(detail.getApplicationName())
                .displayName(detail.getApplicationName())
                .instanceId(0)
                .businessType(param.getBusinessType())
                .businessId(0)
                .completed(false)
                .entryKey(StringFormatter.arrayFormat("applicationName:{}", detail.getApplicationName()))
                .valid(true)
                .content(YamlUtils.dump(detail))
                .comment(detail.getComment())
                .build();
    }

}
