package com.baiyi.cratos.wrapper.security;

import com.baiyi.cratos.common.util.RegexSensitiveDataMasker;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.generator.ApiSecurityTestRecord;
import com.baiyi.cratos.domain.view.security.ApiSecurityTestVO;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.BaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/5/11 15:23
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApiSecurityTestRecordWrapper extends BaseDataTableConverter<ApiSecurityTestVO.Record, ApiSecurityTestRecord> implements BaseWrapper<ApiSecurityTestVO.Record> {

    @Override
    public void wrap(ApiSecurityTestVO.Record vo) {
    }

    public void wrap(ApiSecurityTestVO.RecordSummary vo) {
        if (vo == null) {
            return;
        }
        // RegexSensitiveDataMasker
        if (StringUtils.hasText(vo.getUsername()) && vo.getUsername()
                .equals(SessionUtils.getUsername())) {
            // 明文
            return;
        }
        // 脱敏
        vo.setRequestHeaders(RegexSensitiveDataMasker.maskSensitiveData(vo.getRequestHeaders()));
        vo.setRequestBody(RegexSensitiveDataMasker.maskSensitiveData(vo.getRequestBody()));
        vo.setResponseHeaders(RegexSensitiveDataMasker.maskSensitiveData(vo.getResponseHeaders()));
        vo.setResponseBody(RegexSensitiveDataMasker.maskSensitiveData(vo.getResponseBody()));
    }

}