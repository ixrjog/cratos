package com.baiyi.cratos.eds.googlecloud.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
        private List<String> roles;
    }

    public static Member toMember(String string, List<String> roles) {
        String[] strings = string.split(":");
        return Member.builder()
                .type(strings[0])
                .name(strings[1])
                .roles(roles)
                .build();
    }

}
