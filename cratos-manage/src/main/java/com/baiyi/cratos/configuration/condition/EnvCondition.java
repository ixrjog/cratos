package com.baiyi.cratos.configuration.condition;

import lombok.NonNull;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import static com.baiyi.cratos.common.constant.Global.ENV_PROD;

/**
 * @Author baiyi
 * @Date 2024/3/8 18:11
 * @Version 1.0
 */
public class EnvCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, @NonNull AnnotatedTypeMetadata metadata) {
        String env = context.getEnvironment()
                .getActiveProfiles()[0];
        return ENV_PROD.equals(env);
    }

}

