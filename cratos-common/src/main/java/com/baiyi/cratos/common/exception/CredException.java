package com.baiyi.cratos.common.exception;

import com.baiyi.cratos.common.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2024/3/6 16:34
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CredException extends BaseException {

    @Serial
    private static final long serialVersionUID = 8533271697854738627L;
    private Integer code;

    public CredException(String message) {
        super(message);
        this.code = 40000;
    }

    public CredException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = 40000;
    }

}
