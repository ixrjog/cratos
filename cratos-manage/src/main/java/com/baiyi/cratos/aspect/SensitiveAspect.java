package com.baiyi.cratos.aspect;

import com.baiyi.cratos.aspect.sensitive.SensitiveFormatter;
import com.baiyi.cratos.aspect.sensitive.SensitiveFormatterFactory;
import com.baiyi.cratos.domain.annotation.FieldSensitive;
import com.baiyi.cratos.domain.enums.SensitiveType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @Author baiyi
 * @Date 2024/1/8 11:44
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
public class SensitiveAspect {

    @Value("${cratos.sensitive.mask.enabled:true}")
    private boolean enabled;

    @Pointcut(value = "@annotation(com.baiyi.cratos.annotation.Sensitive)")
    public void annotationPoint() {
    }

    @After(value = "annotationPoint()")
    public void doAfter(JoinPoint joinPoint) {
        if (enabled) {
            Arrays.stream(joinPoint.getArgs()).filter(arg -> AopUtils.getTargetClass(arg).getAnnotation(FieldSensitive.class) != null).forEach(this::eraseSensitiveInfo);
        }
    }

    private void eraseSensitiveInfo(Object view) {
        // 获取视图中所有字段
        Field[] fields = view.getClass().getDeclaredFields();
        Arrays.stream(fields).filter(field -> field.isAnnotationPresent(FieldSensitive.class)).forEach(field -> {
            // 获取字段注解
            FieldSensitive fieldSensitive = field.getAnnotation(FieldSensitive.class);
            // 获取脱敏类型
            SensitiveType sensitiveType = fieldSensitive.type();
            SensitiveFormatter sensitiveFormatter = SensitiveFormatterFactory.getFormatter(sensitiveType.name());
            if (sensitiveFormatter != null) {
                try {
                    field.setAccessible(true);
                    // 字段脱敏
                    field.set(view, sensitiveFormatter.format(fieldSensitive, (String) field.get(view)));
                } catch (IllegalAccessException ignored) {
                }
            }
        });
    }

}
