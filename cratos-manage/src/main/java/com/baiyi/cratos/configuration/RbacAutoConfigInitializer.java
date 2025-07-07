package com.baiyi.cratos.configuration;

import com.baiyi.cratos.common.configuration.CratosConfiguration;
import com.baiyi.cratos.common.configuration.model.CratosModel;
import com.baiyi.cratos.domain.generator.RbacGroup;
import com.baiyi.cratos.domain.generator.RbacResource;
import com.baiyi.cratos.domain.util.SpringContextUtils;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RBAC自动配置初始化器
 * 扫描控制器并自动创建RBAC资源和组
 *
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
    private static final Set<RequestMethod> NON_VALID_METHODS = EnumSet.of(RequestMethod.GET);

    // 缓存已创建的组，避免重复查询数据库
    private final Map<String, RbacGroup> groupCache = new ConcurrentHashMap<>();

    @Override
    public void run(String... args) {
        if (!isRbacAutoConfigurationEnabled()) {
            log.debug("RBAC auto configuration is disabled");
            return;
        }

        long startTime = System.currentTimeMillis();
        log.info("Starting RBAC auto configuration...");

        try {
            ConfigurationResult result = initializeRbacResources();
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            log.info(
                    "RBAC auto configuration completed successfully. " + "Processed {} controllers, created {} groups, {} resources. " + "Total time: {}ms",
                    result.getProcessedControllers(), result.getCreatedGroups(), result.getCreatedResources(),
                    duration);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            log.error("Failed to initialize RBAC auto configuration after {}ms", duration, e);
        }
    }

    /**
     * 检查RBAC自动配置是否启用
     */
    private boolean isRbacAutoConfigurationEnabled() {
        return Optional.ofNullable(cratosConfiguration)
                .map(CratosConfiguration::getRbac)
                .map(CratosModel.Rbac::getAutoConfiguration)
                .map(CratosModel.AutoConfiguration::getEnabled)
                .orElse(false);
    }

    /**
     * 初始化RBAC资源
     */
    private ConfigurationResult initializeRbacResources() {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        ConfigurationResult result = new ConfigurationResult();

        handlerMethods.entrySet()
                .parallelStream()
                .filter(entry -> isTargetController(entry.getValue()))
                .forEach(entry -> {
                    result.incrementProcessedControllers();
                    processHandlerMethod(entry.getKey(), entry.getValue(), result);
                });
        return result;
    }

    /**
     * 判断是否为目标控制器
     */
    private boolean isTargetController(HandlerMethod handlerMethod) {
        String controllerName = handlerMethod.getBeanType()
                .getName();
        return Arrays.stream(SCAN_PACKAGES)
                .anyMatch(controllerName::startsWith);
    }

    /**
     * 处理单个处理器方法
     */
    private void processHandlerMethod(RequestMappingInfo mappingInfo, HandlerMethod handlerMethod,
                                      ConfigurationResult result) {
        try {
            ControllerMethodMapping mapping = buildControllerMethodMapping(handlerMethod);
            if (mapping == null || !mapping.isValid()) {
                return;
            }

            RbacGroup rbacGroup = getOrCreateGroup(mapping, result);
            createRbacResource(mapping, rbacGroup, result);

        } catch (Exception e) {
            log.warn("Failed to process handler method: {}", handlerMethod.getMethod()
                    .getName(), e);
        }
    }

    /**
     * 构建控制器方法映射
     */
    private ControllerMethodMapping buildControllerMethodMapping(HandlerMethod handlerMethod) {
        Object controller = SpringContextUtils.getBean(handlerMethod.getBean()
                .toString());
        Class<?> targetClass = AopUtils.getTargetClass(controller);

        // 获取Tag注解
        Tag tag = targetClass.getAnnotation(Tag.class);
        if (tag == null || !StringUtils.hasText(tag.name())) {
            log.debug("Controller {} has no valid Tag annotation", targetClass.getSimpleName());
            return null;
        }

        // 获取RequestMapping注解
        RequestMapping requestMapping = targetClass.getAnnotation(RequestMapping.class);
        String basePath = (requestMapping != null && requestMapping.value().length > 0) ? requestMapping.value()[0] : "";

        ControllerMethodMapping.ControllerMethodMappingBuilder builder = ControllerMethodMapping.builder()
                .tag(tag.name())
                .base(basePath);

        // 处理方法注解
        processMethodAnnotations(handlerMethod, builder);
        return builder.build();
    }

    /**
     * 处理方法注解
     */
    private void processMethodAnnotations(HandlerMethod handlerMethod,
                                          ControllerMethodMapping.ControllerMethodMappingBuilder builder) {
        Annotation[] annotations = handlerMethod.getMethod()
                .getDeclaredAnnotations();

        for (Annotation annotation : annotations) {
            processAnnotation(annotation, builder);
        }
    }

    /**
     * 处理单个注解
     */
    private void processAnnotation(Annotation annotation,
                                   ControllerMethodMapping.ControllerMethodMappingBuilder builder) {
        switch (annotation) {
            case Operation operation -> builder.summary(operation.summary());
            case PostMapping postMapping -> {
                builder.requestMethod(RequestMethod.POST.name());
                if (postMapping.value().length > 0) {
                    builder.methodValue(postMapping.value()[0]);
                }
            }
            case GetMapping getMapping -> {
                builder.requestMethod(RequestMethod.GET.name());
                if (getMapping.value().length > 0) {
                    builder.methodValue(getMapping.value()[0]);
                }
            }
            case PutMapping putMapping -> {
                builder.requestMethod(RequestMethod.PUT.name());
                if (putMapping.value().length > 0) {
                    builder.methodValue(putMapping.value()[0]);
                }
            }
            case DeleteMapping deleteMapping -> {
                builder.requestMethod(RequestMethod.DELETE.name());
                if (deleteMapping.value().length > 0) {
                    builder.methodValue(deleteMapping.value()[0]);
                }
            }
            case PatchMapping patchMapping -> {
                builder.requestMethod(RequestMethod.PATCH.name());
                if (patchMapping.value().length > 0) {
                    builder.methodValue(patchMapping.value()[0]);
                }
            }
            default -> {
                // 其他注解暂不处理
            }
        }
    }

    /**
     * 获取或创建RBAC组
     */
    private RbacGroup getOrCreateGroup(ControllerMethodMapping mapping, ConfigurationResult result) {
        String cacheKey = mapping.getTag() + ":" + mapping.getBase();
        return groupCache.computeIfAbsent(cacheKey, key -> {
            RbacGroup rbacGroup = RbacGroup.builder()
                    .groupName(mapping.getTag())
                    .base(mapping.getBase())
                    .comment("")
                    .build();
            RbacGroup existingGroup = rbacGroupService.getByUniqueKey(rbacGroup);
            if (existingGroup != null) {
                return existingGroup;
            }
            rbacGroupService.add(rbacGroup);
            result.incrementCreatedGroups();
            return rbacGroup;
        });
    }

    /**
     * 创建RBAC资源
     */
    private void createRbacResource(ControllerMethodMapping mapping, RbacGroup rbacGroup, ConfigurationResult result) {
        String resourceName = Joiner.on("")
                .skipNulls()
                .join(mapping.getBase(), mapping.getMethodValue());
        boolean isValid = !NON_VALID_METHODS.contains(RequestMethod.valueOf(mapping.getRequestMethod()));
        RbacResource rbacResource = RbacResource.builder()
                .groupId(rbacGroup.getId())
                .resourceName(resourceName)
                .comment(StringUtils.hasText(mapping.getSummary()) ? mapping.getSummary() : "")
                .valid(isValid)
                .uiPoint(false)
                .build();
        // 检查资源是否已存在
        if (rbacResourceService.getByUniqueKey(rbacResource) == null) {
            rbacResourceService.add(rbacResource);
            result.incrementCreatedResources();
            log.debug("Created RBAC resource: {}", resourceName);
        }
    }

    /**
     * 配置结果统计类
     */
    @Data
    public static class ConfigurationResult {
        private int processedControllers = 0;
        private int createdGroups = 0;
        private int createdResources = 0;
        public synchronized void incrementProcessedControllers() {
            this.processedControllers++;
        }
        public synchronized void incrementCreatedGroups() {
            this.createdGroups++;
        }
        public synchronized void incrementCreatedResources() {
            this.createdResources++;
        }
    }

    /**
     * 控制器方法映射数据类
     */
    @Data
    @Builder
    public static class ControllerMethodMapping {
        private String base;
        private String tag;
        private String summary;
        private String requestMethod;
        private String methodValue;

        /**
         * 验证映射是否有效
         */
        public boolean isValid() {
            return StringUtils.hasText(tag) && StringUtils.hasText(requestMethod) && StringUtils.hasText(methodValue);
        }
    }

}
