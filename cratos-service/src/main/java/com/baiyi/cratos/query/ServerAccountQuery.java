package com.baiyi.cratos.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/10 14:58
 * &#064;Version 1.0
 */
public class ServerAccountQuery {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QueryUserPermissionServerAccountParam implements Serializable {
        @Serial
        private static final long serialVersionUID = 1635706739259839444L;
        private List<Integer> userPermissionIds;
        private List<Integer> hasEdsTagIds;
    }

}
