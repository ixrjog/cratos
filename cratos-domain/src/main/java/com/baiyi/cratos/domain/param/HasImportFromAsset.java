package com.baiyi.cratos.domain.param;

import com.baiyi.cratos.domain.BaseBusiness;

/**
 * @Author baiyi
 * @Date 2024/3/13 10:37
 * @Version 1.0
 */
public interface HasImportFromAsset extends BaseBusiness.IBusinessTypeAnnotate {
    Integer getFromAssetId();
}
