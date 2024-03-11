package com.baiyi.cratos.common.exception;

import com.baiyi.cratos.common.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2024/3/11 09:47
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CustomSchedulerException extends BaseException {

    @Serial
    private static final long serialVersionUID = -2371442471548080976L;

    private Integer code;

    public CustomSchedulerException(String message) {
        super(message);
        this.code = 80000;
    }

    public CustomSchedulerException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = 80000;
    }

}

