package com.baiyi.cratos.common.exception;

import com.baiyi.cratos.common.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.baiyi.cratos.common.exception.constant.ErrorCodeConstants.RISK_EVENT_ERROR;

/**
 * @Author baiyi
 * @Date 2024/4/15 下午5:18
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class RiskEventImpactException extends BaseException {

    @Serial
    private static final long serialVersionUID = -1680408354663291539L;
    private int code;

    public RiskEventImpactException(String message) {
        super(message);
        this.code = RISK_EVENT_ERROR;
    }

    public RiskEventImpactException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = RISK_EVENT_ERROR;
    }

}
