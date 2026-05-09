package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.RedisUtil;
import com.baiyi.cratos.domain.param.http.security.ApiTestParam;
import com.baiyi.cratos.eds.security.apirisk.test.enums.PrivateKeyType;
import com.baiyi.cratos.eds.security.apirisk.test.enums.SignatureAlgorithmEnum;
import com.baiyi.cratos.eds.security.apirisk.test.generic.GenericCallService;
import com.baiyi.cratos.eds.security.apirisk.test.generic.HttpRequestParser;
import com.baiyi.cratos.eds.security.apirisk.test.model.AutoSign;
import com.baiyi.cratos.eds.security.apirisk.test.model.GenericCall;
import com.baiyi.cratos.eds.security.apirisk.test.signature.SignatureAlgorithm;
import com.baiyi.cratos.eds.security.apirisk.test.signature.SignatureFactory;
import com.baiyi.cratos.facade.ApiSecurityTestFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/5/7 11:22
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class ApiSecurityTestFacadeImpl implements ApiSecurityTestFacade {

    private final GenericCallService genericCallService;
    private final RedisUtil redisUtil;

    @Override
    public GenericCall.Response callTestApi(ApiTestParam.CallApi callApi) {
        GenericCall.Request request = HttpRequestParser.parse(callApi.getRequestMessage());
        // init
        resetTimestamp(request);
        setToken(callApi, request);
        removeSign(request);
        SignatureAlgorithmEnum signatureAlgorithmEnum = SignatureAlgorithmEnum.valueOf(callApi.getSignatureAlgorithm());
        SignatureAlgorithm signatureAlgorithm = SignatureFactory.getSignatureAlgorithm(signatureAlgorithmEnum);
        Map<String, String> signMap = signatureAlgorithm.calcSign(
                request, PrivateKeyType.valueOf(callApi.getPrivateKeyType()));
        request.getHeaders()
                .putAll(signMap);

        Mono<GenericCall.Response> response = genericCallService.callDynamicApiWithResponse(
                request.getUrl(),
                request.getMethod(),
                request.getHeaders(),
                request.getBodyStr(),
                callApi.getOriginServer()
        );
        //      System.out.println(JSONUtils.writeValueAsPrettyString(response.block()));
        return response.block();
    }

    protected void removeSign(GenericCall.Request request) {
        request.getHeaders()
                .remove("pp_req_sign");
        request.getHeaders()
                .remove("pp_req_sign_2");
        request.getHeaders()
                .remove("pp_req_sign_v2");
    }

    protected void resetTimestamp(GenericCall.Request request) {
        request.getHeaders()
                .put("pp_timestamp", String.valueOf(System.currentTimeMillis()));
    }

    protected void setToken(ApiTestParam.CallApi callApi, GenericCall.Request request) {
        if (SignatureAlgorithmEnum.PARTNERAPPSIGN.name()
                .equals(callApi.getSignatureAlgorithm())) {
            request.getHeaders()
                    .put("token", callApi.getPpToken());
        } else {
            if (StringUtils.hasText(callApi.getPpToken())) {
                request.getHeaders()
                        .put("pp_token", callApi.getPpToken());
            }
        }
    }

    private static final String SIGN_MAP_KEY = "API:SEC:TEST:AUTO:SIGN:STR";

    @Override
    public void saveAutoSignMap(ApiTestParam.SaveSignMap saveSignMap) {
        // 校验 YAML 格式
        AutoSign.loadAs(saveSignMap.getSignMapYaml());
        redisUtil.set(SIGN_MAP_KEY, saveSignMap.getSignMapYaml());
    }

    @Override
    public String getAutoSignMapYaml() {
        Object obj = redisUtil.get(SIGN_MAP_KEY);
        return obj == null ? "" : (String) obj;
    }

}
