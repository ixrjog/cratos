package com.baiyi.cratos.common.exception;

import com.baiyi.cratos.domain.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.baiyi.cratos.common.exception.constant.ErrorCodeConstants.MENU_ERROR;

/**
 * @Author baiyi
 * @Date 2024/4/10 下午5:37
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class MenuException extends BaseException {

    @Serial
    private static final long serialVersionUID = 7901118355913075951L;

    private int code;

    public MenuException(String message) {
        super(message);
        this.code = MENU_ERROR;
    }

    public MenuException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = MENU_ERROR;
    }

}
