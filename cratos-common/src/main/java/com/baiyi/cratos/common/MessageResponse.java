package com.baiyi.cratos.common;

import com.baiyi.cratos.domain.util.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/20 11:36
 * &#064;Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse<T> {

    private T body;

    private String messageType;

    @Override
    public String toString() {
        return JSONUtil.writeValueAsString(this);
    }

}