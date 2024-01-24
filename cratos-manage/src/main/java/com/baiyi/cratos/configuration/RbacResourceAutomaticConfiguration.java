package com.baiyi.cratos.configuration;

import com.baiyi.cratos.common.configuration.CratosConfiguration;
import com.baiyi.cratos.domain.generator.RbacGroup;
import com.baiyi.cratos.domain.generator.RbacResource;
import com.baiyi.cratos.domain.util.SpringContextUtil;
import com.baiyi.cratos.service.RbacGroupService;
import com.baiyi.cratos.service.RbacResourceService;
import com.google.common.base.Joiner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2024/1/24 10:19
 * @Version 1.0
 */
@Slf4j
@Component
public class RbacResourceAutomaticConfiguration implements CommandLineRunner {

    @Resource
    private RequestMappingHandlerMapping handlerMapping;

    @Resource
    private RbacGroupService rbacGroupService;

    @Resource
    private RbacResourceService rbacResourceService;

    @Resource
    private CratosConfiguration cratosConfiguration;

    private static final String[] SCAN_PACKAGES = {"com.baiyi.cratos.controller"};

    @Override
    public void run(String... args) throws Exception {
        boolean enabledRbacAutoConfiguration = Optional.of(cratosConfiguration)
                .map(CratosConfiguration::getRbac)
                .map(CratosConfiguration.Rbac::getAutoConfiguration)
                .map(CratosConfiguration.AutoConfiguration::getEnabled)
                .orElse(false);
        if (enabledRbacAutoConfiguration) {
            log.info("RBAC resource autoConfiguration.");
            start();
        }
    }

    public void start() {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        // 输出Controller信息
        handlerMethods.forEach((mappingInfo, method) -> {
            if (matchController(method.getBeanType()
                    .getName())) {
                // log.debug("Controller Class: {}", method.getBeanType().getName());
                handle(mappingInfo, method);
            }
        });
    }

    @Data
    @Builder
    public static class ControllerMethodMapping {
        private String base;
        private String tag;
        private String summary;
        private String requestMethod;
        private String methodValue;
    }

    private void handle(RequestMappingInfo mappingInfo, HandlerMethod method) {
        // 获取方法上的注解
        Annotation[] declaredAnnotations = method.getMethod()
                .getDeclaredAnnotations();
        ControllerMethodMapping controllerMethodMapping = ControllerMethodMapping.builder()
                .build();
        Object controller = SpringContextUtil.getBean(method.getBean()
                .toString());
        Tag tag = AopUtils.getTargetClass(controller)
                .getAnnotation(Tag.class);
        controllerMethodMapping.setTag(tag.name());
        RequestMapping requestMapping = AopUtils.getTargetClass(controller)
                .getAnnotation(RequestMapping.class);
        if (requestMapping != null) {
            controllerMethodMapping.setBase(requestMapping.value()[0]);
        }
        for (Annotation declaredAnnotation : declaredAnnotations) {
            if (declaredAnnotation instanceof Operation operation) {
                controllerMethodMapping.setSummary(operation.summary());
                continue;
            }
            if (declaredAnnotation instanceof PostMapping postMapping) {
                controllerMethodMapping.setRequestMethod("POST");
                controllerMethodMapping.setMethodValue(postMapping.value()[0]);
                continue;
            }
            if (declaredAnnotation instanceof GetMapping getMapping) {
                controllerMethodMapping.setRequestMethod("GET");
                controllerMethodMapping.setMethodValue(getMapping.value()[0]);
                continue;
            }
            if (declaredAnnotation instanceof PutMapping putMapping) {
                controllerMethodMapping.setRequestMethod("PUT");
                controllerMethodMapping.setMethodValue(putMapping.value()[0]);
                continue;
            }
            if (declaredAnnotation instanceof DeleteMapping deleteMapping) {
                controllerMethodMapping.setRequestMethod("DELETE");
                controllerMethodMapping.setMethodValue(deleteMapping.value()[0]);
            }
        }
        RbacGroup rbacGroup = RbacGroup.builder()
                .groupName(controllerMethodMapping.getTag())
                .base(controllerMethodMapping.getBase())
                .comment("")
                .build();
        RbacGroup dBRbacGroup = rbacGroupService.getByUniqueKey(rbacGroup);
        if (dBRbacGroup != null) {
            rbacGroup.setId(dBRbacGroup.getId());
        }
        RbacResource rbacResource = RbacResource.builder()
                .groupId(rbacGroup.getId())
                .resourceName(Joiner.on("")
                        .skipNulls()
                        .join(controllerMethodMapping.getBase(), controllerMethodMapping.getMethodValue()))
                .comment(controllerMethodMapping.getSummary())
                .valid(!controllerMethodMapping.getRequestMethod()
                        .equalsIgnoreCase("GET"))
                .uiPoint(false)
                .build();
        if (rbacResourceService.getByUniqueKey(rbacResource) == null) {
            rbacResourceService.add(rbacResource);
        }
    }

    private boolean matchController(String controllerName) {
        return Arrays.stream(SCAN_PACKAGES)
                .anyMatch(controllerName::startsWith);
    }

}
