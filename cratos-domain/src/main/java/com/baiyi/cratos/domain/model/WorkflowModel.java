package com.baiyi.cratos.domain.model;

import com.baiyi.cratos.domain.YamlDump;
import com.baiyi.cratos.domain.view.user.UserVO;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/18 10:47
 * &#064;Version 1.0
 */
public class WorkflowModel {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Workflow extends YamlDump implements Serializable {
        @Serial
        private static final long serialVersionUID = -950956814213648037L;
        public static final Workflow NO_DATA = Workflow.builder()
                .build();
        @Builder.Default
        private List<Node> nodes = List.of();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Node {
        private String name;
        private String approvalType;
        private String comment;
        private Map<String, I18nModel.Alias> langMap;
        private List<String> tags;
        private List<UserVO.User> selectableUsers;
    }

}
