package com.baiyi.cratos.facade.inspection.model;

import lombok.Builder;
import lombok.Data;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/24 15:02
 * &#064;Version 1.0
 */
public class ResignedUserModel {

    @Data
    @Builder
    public static class User {
        String username;
        String name;
        String displayName;
        String email;
    }

}
