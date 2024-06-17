package com.baiyi.cratos.common.util;

import com.google.common.collect.Lists;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.support.CronExpression;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2022/3/23 17:34
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public final class CronUtil {

    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 解析CRON表达式，展示最近N次执行时间
     * @param expression
     * @param cnt
     * @return
     */
    public static List<String> recentTime(String expression, int cnt) {
        List<String> result = Lists.newArrayList();
        try {
            final CronExpression cronExpression = CronExpression.parse(expression);
            LocalDateTime dateTime = LocalDateTime.now();
            while (1 <= cnt) {
                dateTime = cronExpression.next(dateTime);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern(PATTERN);
                assert dateTime != null;
                result.add(dtf.format(dateTime));
                cnt--;
            }
        } catch (Exception ignored) {
        }
        return result;
    }

}