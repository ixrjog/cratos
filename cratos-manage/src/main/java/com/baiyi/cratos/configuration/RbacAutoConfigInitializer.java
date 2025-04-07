package com.baiyi.cratos.configuration;

import com.baiyi.cratos.common.configuration.CratosConfiguration;
import com.baiyi.cratos.common.configuration.model.CratosModel;
import com.baiyi.cratos.domain.generator.RbacGroup;
import com.baiyi.cratos.domain.generator.RbacResource;
import com.baiyi.cratos.domain.util.SpringContextUtil;
import com.baiyi.cratos.service.RbacGroupService;
import com.baiyi.cratos.service.RbacResourceService;
import com.google.common.base.Joiner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class RbacAutoConfigInitializer implements CommandLineRunner {

    private final RequestMappingHandlerMapping handlerMapping;
    private final RbacGroupService rbacGroupService;
    private final RbacResourceService rbacResourceService;
    private final CratosConfiguration cratosConfiguration;
    private static final String[] SCAN_PACKAGES = {"com.baiyi.cratos.controller"};

    @Override
    public void run(String... args) {
        boolean enabledRbacAutoConfiguration = Optional.of(cratosConfiguration)
                .map(CratosConfiguration::getRbac)
                .map(CratosModel.Rbac::getAutoConfiguration)
                .map(CratosModel.AutoConfiguration::getEnabled)
                .orElse(false);
        if (enabledRbacAutoConfiguration) {
            log.info("RBAC auto configuration.");
            start();
        }
    }

    /**
     * test
     */
    public void start() {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        handlerMethods.forEach((mappingInfo, method) -> {
            if (matchController(method.getBeanType()
                    .getName())) {
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
        // 从 controller 上获取 Tag 注解
        Tag tag = AopUtils.getTargetClass(controller)
                .getAnnotation(Tag.class);
        if (tag == null) {
            return;
        }
        controllerMethodMapping.setTag(tag.name());
        RequestMapping requestMapping = AopUtils.getTargetClass(controller)
                .getAnnotation(RequestMapping.class);
        if (requestMapping != null) {
            controllerMethodMapping.setBase(requestMapping.value()[0]);
        }
        invoke(controllerMethodMapping, declaredAnnotations);
        RbacGroup rbacGroup = getOrCreateGroup(controllerMethodMapping);
        boolean valid = !RequestMethod.GET.name()
                .equalsIgnoreCase(controllerMethodMapping.getRequestMethod());
        RbacResource rbacResource = RbacResource.builder()
                .groupId(rbacGroup.getId())
                .resourceName(Joiner.on("")
                        .skipNulls()
                        .join(controllerMethodMapping.getBase(), controllerMethodMapping.getMethodValue()))
                .comment(controllerMethodMapping.getSummary())
                .valid(valid)
                .uiPoint(false)
                .build();
        if (rbacResourceService.getByUniqueKey(rbacResource) == null) {
            rbacResourceService.add(rbacResource);
        }
    }

    private RbacGroup getOrCreateGroup(ControllerMethodMapping controllerMethodMapping) {
        RbacGroup rbacGroup = RbacGroup.builder()
                .groupName(controllerMethodMapping.getTag())
                .base(controllerMethodMapping.getBase())
                .comment("")
                .build();
        RbacGroup dBRbacGroup = rbacGroupService.getByUniqueKey(rbacGroup);
        if (dBRbacGroup != null) {
            return dBRbacGroup;
        } else {
            rbacGroupService.add(rbacGroup);
            return rbacGroup;
        }
    }

    private void invoke(ControllerMethodMapping controllerMethodMapping, Annotation[] declaredAnnotations) {
        Arrays.stream(declaredAnnotations)
                .forEach(declaredAnnotation -> {
                    switch (declaredAnnotation) {
                        case Operation operation -> {
                            controllerMethodMapping.setSummary(operation.summary());
                        }
                        case PostMapping postMapping -> {
                            controllerMethodMapping.setRequestMethod(RequestMethod.POST.name());
                            controllerMethodMapping.setMethodValue(postMapping.value()[0]);
                        }
                        case GetMapping getMapping -> {
                            controllerMethodMapping.setRequestMethod(RequestMethod.GET.name());
                            controllerMethodMapping.setMethodValue(getMapping.value()[0]);
                        }
                        case PutMapping putMapping -> {
                            controllerMethodMapping.setRequestMethod(RequestMethod.PUT.name());
                            controllerMethodMapping.setMethodValue(putMapping.value()[0]);
                        }
                        case DeleteMapping deleteMapping -> {
                            controllerMethodMapping.setRequestMethod(RequestMethod.DELETE.name());
                            controllerMethodMapping.setMethodValue(deleteMapping.value()[0]);
                        }
                        case null, default -> {
                        }
                    }
                });
    }

    private boolean matchController(String controllerName) {
        return Arrays.stream(SCAN_PACKAGES)
                .anyMatch(controllerName::startsWith);
    }

}
