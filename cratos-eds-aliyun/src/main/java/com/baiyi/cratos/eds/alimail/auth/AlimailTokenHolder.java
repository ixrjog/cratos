package com.baiyi.cratos.eds.alimail.auth;

import com.baiyi.cratos.eds.alimail.model.AlimailToken;
import com.baiyi.cratos.eds.core.config.model.EdsAlimailConfigModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.SHORT_TERM;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/11 14:42
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class AlimailTokenHolder {

    /**
     * API Docs
     * https://mailhelp.aliyun.com/openapi/index.html#/markdown/authorization.md
     * @param alimail
     * @return
     */
    @Cacheable(cacheNames = SHORT_TERM, key = "'ALIMAIL:V0:TOKEN:APPID:'+ #alimail.cred.id", unless = "#result == null")
    public AlimailToken.Token getToken(EdsAlimailConfigModel.Alimail alimail) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "client_credentials");
        formData.add("client_id", alimail.getCred()
                .getId());
        formData.add("client_secret", alimail.getCred()
                .getSecret());
        final String api = alimail.getApi().getUrl() + "/oauth2/v2.0/token";
        return WebClient.create()
                .post()
                .uri(api)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(AlimailToken.Token.class).block();
    }

    @Cacheable(cacheNames = SHORT_TERM, key = "'ALIMAIL:V0:BEARER:APPID:'+ #alimail.cred.id", unless = "#result == null")
    public String getBearer(EdsAlimailConfigModel.Alimail alimail) {
        return getToken(alimail).toBearer();
    }

}
