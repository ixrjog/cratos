package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.facade.WebAuthnFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/webauthn")
@Tag(name = "WebAuthn")
@RequiredArgsConstructor
public class WebAuthnController {

    private final WebAuthnFacade webAuthnFacade;

    @Operation(summary = "Get registration options (after login)")
    @GetMapping(value = "/register/options", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Map<String, Object>> getRegistrationOptions() {
        return HttpResult.of(webAuthnFacade.getRegistrationOptions());
    }

    @Operation(summary = "Complete registration")
    @PostMapping(value = "/register/complete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> completeRegistration(@RequestBody Map<String, Object> credential) {
        webAuthnFacade.completeRegistration(credential);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Get login options")
    @GetMapping(value = "/login/options", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Map<String, Object>> getLoginOptions(
            @RequestParam(required = false) String username) {
        if (username == null || username.isBlank()) {
            return HttpResult.of(webAuthnFacade.getLoginOptions());
        }
        return HttpResult.of(webAuthnFacade.getLoginOptions(username));
    }

    @Operation(summary = "Complete login")
    @PostMapping(value = "/login/complete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Map<String, Object>> completeLogin(@RequestBody Map<String, Object> assertion) {
        return HttpResult.of(webAuthnFacade.completeLogin(assertion));
    }

    @Operation(summary = "List user's registered credentials")
    @GetMapping(value = "/credentials", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<?> listCredentials() {
        return HttpResult.of(webAuthnFacade.listMyCredentials());
    }

    @Operation(summary = "Delete credential")
    @DeleteMapping(value = "/credential/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteCredential(@RequestParam int id) {
        webAuthnFacade.deleteCredential(id);
        return HttpResult.SUCCESS;
    }

}
