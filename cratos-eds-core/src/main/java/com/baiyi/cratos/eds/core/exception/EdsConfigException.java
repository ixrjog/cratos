package com.baiyi.cratos.eds.core.exception;

import com.baiyi.cratos.common.exception.BaseException;
import com.baiyi.cratos.common.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2024/2/27 14:14
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class EdsConfigException extends BaseException {

    @Serial
    private static final long serialVersionUID = 970944638016396476L;

    private int code;

    public EdsConfigException(String message) {
        super(message);
        this.code = 70000;
    }

    public EdsConfigException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = 70000;
    }

}