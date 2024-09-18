package com.baiyi.cratos.aspect;

import com.baiyi.cratos.annotation.PreVerifyPermissionsFromParam;
import com.baiyi.cratos.common.enums.AccessLevel;
import com.baiyi.cratos.common.exception.auth.AuthenticationException;
import com.baiyi.cratos.domain.ErrorEnum;
import com.baiyi.cratos.domain.generator.RbacRole;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.facade.rbac.RbacRoleFacade;
import com.baiyi.cratos.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/18 15:00
 * &#064;Version 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class PreVerifyPermissionsFromParamAspect {

    private final DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
    private final ExpressionParser expressionParser = new SpelExpressionParser();
    private final UserService userService;
    private final RbacRoleFacade rbacRoleFacade;

    @Pointcut(value = "@annotation(com.baiyi.cratos.annotation.PreVerifyPermissionsFromParam)")
    public void annotationPoint() {
    }

    @Before(value = "@annotation(verifyPermissionsFromParam)")
    public void beforeAdvice(JoinPoint joinPoint, PreVerifyPermissionsFromParam verifyPermissionsFromParam) {
        //获取切面方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //获取方法的形参名称
        String[] params = discoverer.getParameterNames(method);
        //获取方法的实际参数值
        Object[] arguments = joinPoint.getArgs();
        //设置解析SpEL所需的数据上下文
        EvaluationContext context = new StandardEvaluationContext();
        IntStream.range(0, Objects.requireNonNull(params).length)
                .forEachOrdered(len -> context.setVariable(params[len], arguments[len]));
        //解析表达式并获取SpEL的值
        Expression expression = expressionParser.parseExpression(verifyPermissionsFromParam.ofUsername());
        Object usernameParam = expression.getValue(context);
        if (usernameParam instanceof String username) {
            if (!StringUtils.hasText(username)) {
                throw new AuthenticationException(ErrorEnum.UNABLE_TO_OBTAIN_USERNAME);
            }
            User user = userService.getByUsername(username);
            if (user == null || !user.getValid()) {
                // 无效的目标用户
                throw new AuthenticationException(ErrorEnum.INVALID_TARGET_USER);
            }
            Authentication authentication = SecurityContextHolder.getContext()
                    .getAuthentication();
            // 会话用户
            String sessionUsername = authentication.getName();
            // 是否本人操作
            if (username.equals(sessionUsername)) {
                return;
            }
            // accessLevel是否符合
            if (verifyRoleAccessLevelByUsername(verifyPermissionsFromParam.accessLevel(), username)) {
                return;
            }
        }
        throw new AuthenticationException(verifyPermissionsFromParam.rejectMessage());
    }

    private boolean verifyRoleAccessLevelByUsername(AccessLevel accessLevel, String username) {
        List<RbacRole> rbacRoles = rbacRoleFacade.queryUserRoles(username);
        if (CollectionUtils.isEmpty(rbacRoles)) {
            return false;
        }
        return rbacRoles.stream()
                .map(RbacRole::getAccessLevel)
                .max(Comparator.comparing(Integer::intValue))
                .orElse(0) >= accessLevel.getLevel();
    }

}