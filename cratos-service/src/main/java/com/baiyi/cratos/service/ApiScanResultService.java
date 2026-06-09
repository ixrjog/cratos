package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.generator.ApiScanResult;
import com.baiyi.cratos.mapper.ApiScanResultMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;

public interface ApiScanResultService extends BaseValidService<ApiScanResult, ApiScanResultMapper>, BaseUniqueKeyService<ApiScanResult, ApiScanResultMapper> {
}
