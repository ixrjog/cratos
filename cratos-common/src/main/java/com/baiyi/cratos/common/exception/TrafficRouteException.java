package com.baiyi.cratos.common.exception;

import com.baiyi.cratos.domain.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.baiyi.cratos.common.exception.constant.ErrorCodeConstants.TRAFFIC_ROUTE_ERROR;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/12 17:17
 * &#064;Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class TrafficRouteException extends BaseException {
    @Serial
    private static final long serialVersionUID = -2895503787892809344L;
    private int code;

    public TrafficRouteException(String message) {
        super(message);
        this.code = TRAFFIC_ROUTE_ERROR;
    }

    public TrafficRouteException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = TRAFFIC_ROUTE_ERROR;
    }

    public static void runtime(String message) {
        throw new TrafficRouteException(message);
    }

    public static void runtime(String message, Object... var2) {
        throw new TrafficRouteException(message, var2);
    }

}