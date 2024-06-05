package com.baiyi.cratos.common.exception;

import com.baiyi.cratos.common.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.baiyi.cratos.common.exception.constant.ErrorCodeConstants.SQL_ERROR;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/5 下午5:05
 * &#064;Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SqlException extends BaseException {

    @Serial
    private static final long serialVersionUID = -2468647149367758079L;

    private int code;

    public SqlException(String message) {
        super(message);
        this.code = SQL_ERROR;
    }

    public SqlException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = SQL_ERROR;
    }

}