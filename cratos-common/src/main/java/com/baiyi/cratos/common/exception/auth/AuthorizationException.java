package com.baiyi.cratos.common.exception.auth;


import com.baiyi.cratos.common.exception.BaseException;
import com.baiyi.cratos.domain.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

import static com.baiyi.cratos.common.exception.constant.ErrorCodeConstants.AUTHORIZATION_403;

/**
 * 权限校验
 *
 * @Author baiyi
 * @Date 2022/12/29 11:49
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AuthorizationException extends BaseException {

    @Serial
    private static final long serialVersionUID = 8005430143499358990L;
    private Integer code;

    public AuthorizationException(String message) {
        super(message);
        this.code = AUTHORIZATION_403;
    }

    public AuthorizationException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public AuthorizationException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.code = errorEnum.getCode();
    }

}