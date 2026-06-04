package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.view.security.LeakedCredentialVO;
import com.baiyi.cratos.facade.security.SecurityCredentialLeakFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2026/5/21
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/security/credential-leak")
@Tag(name = "Credential Leak Detection")
@RequiredArgsConstructor
public class CredentialLeakController {

    private final SecurityCredentialLeakFacade credentialLeakFacade;

    @Operation(summary = "Detect credential leak")
    @PostMapping(value = "/detect", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<LeakedCredentialVO.Credentials> detectLeak(@RequestBody Map<String, String> body) {
        return new HttpResult<>(credentialLeakFacade.detectLeak(body.getOrDefault("credential", "")));
    }

}
