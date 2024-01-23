package com.baiyi.cratos.common.exception;

import com.baiyi.cratos.common.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2024/1/23 16:12
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class BusinessException extends BaseException {

    @Serial
    private static final long serialVersionUID = -3384211631717079399L;

    private Integer code;

    public BusinessException(String message) {
        super(message);
        this.code = 40000;
    }

    public BusinessException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = 40000;
    }

}
