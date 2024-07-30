package com.baiyi.cratos.eds.googlecloud.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author 修远
 * @Date 2024/7/30 上午11:09
 * @Since 1.0
 */
public class GoogleMemberModel {

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Member {
        private String name;
        private String type;
    }

    public static Member toMember(String string) {
        String[] strings = string.split(":");
        return Member.builder()
                .type(strings[0])
                .name(strings[1])
                .build();
    }

}
