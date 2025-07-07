package com.baiyi.cratos.eds.business.exception;

import com.baiyi.cratos.common.exception.BaseException;
import com.baiyi.cratos.domain.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.baiyi.cratos.common.exception.constant.ErrorCodeConstants.ASSET_TO_BUSINESS_ERROR;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/26 13:50
 * &#064;Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class AssetToBusinessException extends BaseException {

    @Serial
    private static final long serialVersionUID = -6328637498888741933L;
    private int code;

    public AssetToBusinessException(String message) {
        super(message);
        this.code = ASSET_TO_BUSINESS_ERROR;
    }

    public AssetToBusinessException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = ASSET_TO_BUSINESS_ERROR;
    }

}