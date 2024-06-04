package com.baiyi.cratos.ssh.core.message.output;

import com.baiyi.cratos.domain.util.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/29 下午3:15
 * &#064;Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OutputMessage {

    private String instanceId;
    private String output;

    private String error;

    @Builder.Default
    private int code = 0;

    @Override
    public String toString(){
        return JSONUtil.writeValueAsString(this);
    }

}
