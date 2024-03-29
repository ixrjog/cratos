package com.baiyi.cratos.common.exception;

import com.baiyi.cratos.common.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.baiyi.cratos.common.exception.constant.ErrorCodeConstants.TRAFFIC_LAYER_ERROR;

/**
 * @Author baiyi
 * @Date 2024/3/29 18:02
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class TrafficLayerException extends BaseException {

    @Serial
    private static final long serialVersionUID = -334511700153808260L;

    private int code;

    public TrafficLayerException(String message) {
        super(message);
        this.code = TRAFFIC_LAYER_ERROR;
    }

    public TrafficLayerException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = TRAFFIC_LAYER_ERROR;
    }

}
