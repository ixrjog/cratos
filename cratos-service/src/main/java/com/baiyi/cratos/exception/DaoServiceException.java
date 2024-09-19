package com.baiyi.cratos.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2024/1/23 17:37
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Slf4j
public class DaoServiceException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = -5807490039234051240L;

    private int code;

    public DaoServiceException(String message) {
        super(message);
        this.code = 40001;
        log.debug(message);
    }

}
