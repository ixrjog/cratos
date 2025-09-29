package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.param.http.event.EagleCloudEventParam;
import com.baiyi.cratos.domain.param.http.event.GitLabEventParam;
import com.baiyi.cratos.domain.util.JSONUtils;
import com.baiyi.cratos.facade.eaglecloud.EagleCloudSaseFacade;
import com.baiyi.cratos.facade.gitlab.GitLabFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.Serial;

import static com.baiyi.cratos.domain.constant.Global.AUTHORIZATION;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/27 10:01
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/receive/event")
@Tag(name = "Receive Event")
@RequiredArgsConstructor
@Slf4j
public class ReceiveEventController {

    private final GitLabFacade gitLabFacade;
    private final EagleCloudSaseFacade eagleCloudSaseFacade;
    public static final String GITLAB_TOKEN = "X-Gitlab-Token";

    @Operation(summary = "GitLab API v4 hooks")
    @PostMapping(value = "/gitlab/v4/system/hooks", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> receiveGitLabV4SystemHooks(
            @RequestHeader(GITLAB_TOKEN) @NotNull(message = "Header `X-Gitlab-Token` is null") String gitLabToken,
            @RequestBody @Valid GitLabEventParam.SystemHook systemHook) {
        try {
            gitLabFacade.consumeEvent(systemHook, gitLabToken);
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "EagleCloud SASE webhooks")
    @PostMapping(value = "/eaglecloud/sase/hooks", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> receiveEagleCloudSaseWebHooks(
            @RequestHeader(AUTHORIZATION) @NotNull(message = "Header `Authorization` is null") String authorization,
            @RequestBody EagleCloudEventParam.SaseHook saseHook) {
        try {
            log.info("Eaglecloud sase hooks: {}", JSONUtils.writeValueAsString(saseHook));
            eagleCloudSaseFacade.consumeEvent(saseHook, authorization);
        } catch (Exception e) {
            log.error("Failed to process eaglecloud sase hooks: {}", saseHook, e);
        }
        return HttpResult.SUCCESS;
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public static class InvalidTokenException extends RuntimeException {
        @Serial
        private static final long serialVersionUID = 8790736073780463758L;
    }

}
