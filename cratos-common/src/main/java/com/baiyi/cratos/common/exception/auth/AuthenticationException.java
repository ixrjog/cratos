package com.baiyi.cratos.common.exception.auth;

import com.baiyi.cratos.common.exception.BaseException;
import com.baiyi.cratos.common.exception.constant.ErrorCodeConstants;
import com.baiyi.cratos.domain.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 身份认证
 *
 * @Author baiyi
 * @Date 2021/5/13 4:05 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AuthenticationException extends BaseException {

    @Serial
    private static final long serialVersionUID = 3787610554197575905L;
    private int code;

    public AuthenticationException(String message) {
        super(message);
        this.code = ErrorCodeConstants.AUTHENTICATION_401;
    }

    public AuthenticationException(int code, String message) {
        super(message);
        this.code = ErrorCodeConstants.AUTHENTICATION_401;
    }

    public AuthenticationException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.code = errorEnum.getCode();
    }

}