package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.template.NotificationTemplateParam;
import com.baiyi.cratos.domain.view.template.NotificationTemplateVO;
import com.baiyi.cratos.facade.NotificationTemplateFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/7 上午9:54
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/notification/template")
@Tag(name = "Notification Template")
@RequiredArgsConstructor
public class NotificationTemplateController {

    private final NotificationTemplateFacade notificationTemplateFacade;

    @Operation(summary = "Pagination query notification template")
    @PostMapping(value = "/page/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<NotificationTemplateVO.NotificationTemplate>> queryNotificationTemplatePage(
            @RequestBody @Valid NotificationTemplateParam.NotificationTemplatePageQuery pageQuery) {
        return new HttpResult<>(notificationTemplateFacade.queryNotificationTemplatePage(pageQuery));
    }

    @Operation(summary = "Add notification template")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> AddNotificationTemplate(
            @RequestBody @Valid NotificationTemplateParam.AddNotificationTemplate addNotificationTemplate) {
        notificationTemplateFacade.addNotificationTemplate(addNotificationTemplate);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update notification template")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateNotificationTemplate(
            @RequestBody @Valid NotificationTemplateParam.UpdateNotificationTemplate updateNotificationTemplate) {
        notificationTemplateFacade.updateNotificationTemplate(updateNotificationTemplate);
        return HttpResult.SUCCESS;
    }

}
