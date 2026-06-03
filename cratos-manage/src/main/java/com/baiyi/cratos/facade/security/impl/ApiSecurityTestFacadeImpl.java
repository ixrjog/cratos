package com.baiyi.cratos.facade.security.impl;

import com.baiyi.cratos.common.RedisUtil;
import com.baiyi.cratos.common.exception.ApiSecurityTestException;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.ApiSecurityTestRecord;
import com.baiyi.cratos.domain.param.http.security.ApiTestParam;
import com.baiyi.cratos.domain.util.BeanCopierUtils;
import com.baiyi.cratos.domain.util.JSONUtils;
import com.baiyi.cratos.domain.view.security.ApiSecurityTestVO;
import com.baiyi.cratos.eds.security.apirisk.test.enums.PrivateKeyType;
import com.baiyi.cratos.eds.security.apirisk.test.enums.SignatureAlgorithmEnum;
import com.baiyi.cratos.eds.security.apirisk.test.generic.GenericCallService;
import com.baiyi.cratos.eds.security.apirisk.test.generic.HttpRequestParser;
import com.baiyi.cratos.eds.security.apirisk.test.model.AutoSign;
import com.baiyi.cratos.eds.security.apirisk.test.model.GenericCall;
import com.baiyi.cratos.eds.security.apirisk.test.signature.SignatureAlgorithm;
import com.baiyi.cratos.eds.security.apirisk.test.signature.SignatureFactory;
import com.baiyi.cratos.facade.security.ApiSecurityTestFacade;
import com.baiyi.cratos.service.ApiSecurityTestRecordService;
import com.baiyi.cratos.service.TrafficLayerDomainRecordService;
import com.baiyi.cratos.wrapper.security.ApiSecurityTestRecordWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/5/7 11:22
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApiSecurityTestFacadeImpl implements ApiSecurityTestFacade {

    private final GenericCallService genericCallService;
    private final RedisUtil redisUtil;
    private final ApiSecurityTestRecordService recordService;
    private final TrafficLayerDomainRecordService trafficLayerDomainRecordService;
    private final ApiSecurityTestRecordWrapper apiSecurityTestRecordWrapper;

    private final String[] SIGN_HEADERS = {"pp_req_sign", "pp_req_sign_2", "pp_req_sign_v2", "sign"};
    private final String[] TIMESTAMP_HEADERS = {"pp_timestamp", "timestamp"};

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

        long start = System.currentTimeMillis();
        GenericCall.Response resp = null;
        // URL白名单校验, 防止 SSRF
        String url = request.getUrl();
        if (!StringUtils.hasText(url)) {
            ApiSecurityTestException.runtime("URL is empty");
        }
        String host = "";
        try {
            host = java.net.URI.create(url)
                    .getHost();
        } catch (Exception e) {
            ApiSecurityTestException.runtime("Invalid URL format");
        }
        if (!StringUtils.hasText(host)) {
            ApiSecurityTestException.runtime("Host is empty");
        }
        if (trafficLayerDomainRecordService.queryByRecordName(host)
                .isEmpty()) {
            ApiSecurityTestException.runtime("The test URL={} is not in the whitelist", request.getUrl());
        }
        try {
            Mono<GenericCall.Response> response = genericCallService.callDynamicApiWithResponse(
                    request.getUrl(),
                    request.getMethod(),
                    request.getHeaders(),
                    request.getBodyStr(),
                    callApi.getOriginServer()
            );
            resp = response.block();
        } catch (Exception e) {
            log.error("callTestApi error: {}", e.getMessage());
        }
        long elapsed = System.currentTimeMillis() - start;
        saveRecord(callApi, request, resp, elapsed);
        return resp;
    }

    private void saveRecord(ApiTestParam.CallApi callApi, GenericCall.Request request, GenericCall.Response resp,
                            long elapsed) {
        try {
            String host = URI.create(request.getUrl())
                    .getHost();
            String username = SecurityContextHolder.getContext()
                    .getAuthentication() != null ? SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getName() : "";
            ApiSecurityTestRecord record = ApiSecurityTestRecord.builder()
                    .username(username)
                    .requestUrl(request.getUrl())
                    .requestMethod(request.getMethod())
                    .requestHost(host)
                    .originServer(callApi.getOriginServer())
                    .signatureAlgorithm(callApi.getSignatureAlgorithm())
                    .privateKeyType(callApi.getPrivateKeyType())
                    .requestHeaders(JSONUtils.writeValueAsString(request.getHeaders()))
                    .requestBody(request.getBodyStr())
                    .responseStatus(resp != null ? resp.getStatusCode() : null)
                    .responseHeaders(resp != null ? JSONUtils.writeValueAsString(resp.getHeaders()) : null)
                    .responseBody(resp != null ? resp.getBody() : null)
                    .elapsedMs(elapsed)
                    .success(resp != null && resp.getStatusCode() >= 200 && resp.getStatusCode() < 300)
                    .build();
            recordService.add(record);
        } catch (Exception e) {
            log.error("Save test record error: {}", e.getMessage());
        }
    }

    protected void removeSign(GenericCall.Request request) {
        Arrays.stream(SIGN_HEADERS)
                .forEach(header -> request.getHeaders()
                        .remove(header));
    }

    protected void resetTimestamp(GenericCall.Request request) {
        Arrays.stream(TIMESTAMP_HEADERS)
                .forEach(header -> {
                    if (request.getHeaders()
                            .containsKey(header)) {
                        request.getHeaders()
                                .put(header, String.valueOf(System.currentTimeMillis()));
                    }
                });
    }

    protected void setToken(ApiTestParam.CallApi callApi, GenericCall.Request request) {
        if (!StringUtils.hasText(callApi.getPpToken())) {
            return;
        }
        switch (SignatureAlgorithmEnum.valueOf(callApi.getSignatureAlgorithm())) {
            case ADMINPALMMERCHANTSIGN:
            case PARTNERAPPSIGN:
                request.getHeaders()
                        .put("token", callApi.getPpToken());
                break;
            case APIBUSINESSWEBSIGN:
                request.getHeaders()
                        .put("PP_TOKEN", callApi.getPpToken());
                break;
            case APIPALMPAYH5SIGN:
                request.getHeaders()
                        .put("m_token", callApi.getPpToken());
                break;
            case NILEWEBSIGN:
                request.getHeaders()
                        .put("OP-M-TOKEN", callApi.getPpToken());
                break;
            default:
                request.getHeaders()
                        .put("pp_token", callApi.getPpToken());
                break;
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

    @Override
    public DataTable<ApiSecurityTestVO.Record> queryRecordPage(ApiTestParam.RecordPageQuery pageQuery) {
        DataTable<ApiSecurityTestRecord> table = recordService.queryRecordPage(pageQuery);
        return apiSecurityTestRecordWrapper.wrapToTarget(table);
    }

    @Override
    public ApiSecurityTestVO.RecordSummary getTestRecordSummary(int id) {
        ApiSecurityTestRecord record = recordService.getById(id);
        ApiSecurityTestVO.RecordSummary recordSummary = BeanCopierUtils.copyProperties(
                record, ApiSecurityTestVO.RecordSummary.class);
        apiSecurityTestRecordWrapper.wrap(recordSummary);
        return recordSummary;
    }

}
