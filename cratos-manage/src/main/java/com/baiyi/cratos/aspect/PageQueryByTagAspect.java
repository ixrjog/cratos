package com.baiyi.cratos.aspect;

import com.baiyi.cratos.annotation.PageQueryByTag;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.service.BusinessTagService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/23 上午10:24
 * &#064;Version 1.0
 */
@Aspect
@Component
@RequiredArgsConstructor
public class PageQueryByTagAspect {

    private final BusinessTagService businessTagService;

    @Pointcut(value = "@annotation(com.baiyi.cratos.annotation.PageQueryByTag)")
    public void annotationPoint() {
    }

    @Before(value = "@annotation(pageQueryByTag)")
    public void beforeAdvice(JoinPoint joinPoint, PageQueryByTag pageQueryByTag) {
        Arrays.stream(joinPoint.getArgs())
                .filter(arg -> arg instanceof BusinessTagParam.HasQueryByTag)
                .map(arg -> (BusinessTagParam.HasQueryByTag) arg)
                .forEach(hasQueryByTag -> {
                    if (hasQueryByTag.isQueryByTag()) {
                        BusinessTagParam.QueryByTag queryByTag = hasQueryByTag.getQueryByTag();
                        queryByTag.setBusinessType(pageQueryByTag.typeOf()
                                .name());
                        List<Integer> idList = businessTagService.queryBusinessIdByTag(queryByTag);
                        hasQueryByTag.setIdList(idList);
                    } else {
                        hasQueryByTag.setIdList(Collections.emptyList());
                    }
                });
    }

}
