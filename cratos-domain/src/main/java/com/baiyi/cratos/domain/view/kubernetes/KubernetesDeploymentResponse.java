package com.baiyi.cratos.domain.view.kubernetes;

import com.baiyi.cratos.domain.util.JSONUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/20 上午10:54
 * &#064;Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KubernetesDeploymentResponse<T> {
    private T body;
    private String messageType;
    @Override
    public String toString() {
        return JSONUtils.writeValueAsString(this);
    }
}
