package com.baiyi.cratos.aspect;

import com.baiyi.cratos.annotation.DeleteBoundBusiness;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.service.base.BaseBusinessService;
import com.baiyi.cratos.service.factory.BusinessServiceFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * @Author baiyi
 * @Date 2024/1/12 10:45
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@Order(-1)
public class DeleteBoundBusinessAspect {

    // SpEL
    private final DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
    private final ExpressionParser expressionParser = new SpelExpressionParser();

    @Pointcut(value = "@annotation(com.baiyi.cratos.annotation.DeleteBoundBusiness)")
    public void annotationPoint() {
    }

    /**
     * 删除业务对象
     *
     * @param joinPoint
     * @param deleteBoundBusiness
     */
    @After(value = "@annotation(deleteBoundBusiness)")
    public void afterAdvice(JoinPoint joinPoint, DeleteBoundBusiness deleteBoundBusiness) {
        BusinessType businessType = AopUtils.getTargetClass(joinPoint.getTarget())
                .getAnnotation(BusinessType.class);
        // 缺少注解
        if (businessType == null) {
            return;
        }
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
        Expression expression = expressionParser.parseExpression(deleteBoundBusiness.businessId());
        Object businessIdSpEL = expression.getValue(context);
        if (businessIdSpEL instanceof Integer businessId) {
            Arrays.stream(deleteBoundBusiness.targetTypes())
                    .forEach(serviceType -> doDelete(businessId, businessType, serviceType));
        }
    }

    private void doDelete(int businessId, BusinessType businessType, BusinessTypeEnum serviceTypeEnum) {
        BaseBusinessService<?> baseBusinessService = BusinessServiceFactory.getService(serviceTypeEnum.name());
        if (baseBusinessService == null) {
            return;
        }
        SimpleBusiness simpleBusiness = SimpleBusiness.builder()
                .businessId(businessId)
                .businessType(businessType.type()
                        .name())
                .build();
        log.info("Service delete by business: service={}, businessTyp{}, businessId={}", baseBusinessService.getClass()
                .getSimpleName(), businessType.type()
                .name(), businessId);
        baseBusinessService.deleteByBusiness(simpleBusiness);
    }

}
