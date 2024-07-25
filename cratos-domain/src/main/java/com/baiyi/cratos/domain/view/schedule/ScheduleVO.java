package com.baiyi.cratos.domain.view.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/3/11 09:59
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class ScheduleVO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Job {
        private String name;
        private String group;
        private String status;
        private String description;
        private String cronExpression;
        private List<String> executionTime;
    }

}
