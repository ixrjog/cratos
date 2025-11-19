package com.baiyi.cratos.aspect;

import com.baiyi.cratos.annotation.PostImportProcessor;
import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.common.util.PasswordGenerator;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessAssetBind;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.param.HasImportFromAsset;
import com.baiyi.cratos.domain.view.ToBusinessTarget;
import com.baiyi.cratos.processor.PostImportAssetProcessorFactory;
import com.baiyi.cratos.eds.core.EdsInstanceQueryHelper;
import com.baiyi.cratos.eds.core.facade.EdsIdentityFacade;
import com.baiyi.cratos.service.BusinessAssetBindService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.UserService;
import com.google.api.client.util.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2024/3/13 11:04
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class PostImportProcessorAspect {

    private final EdsAssetService edsAssetService;
    private final EdsInstanceQueryHelper edsInstanceQueryHelper;
    private final UserService userService;
    private final BusinessAssetBindService businessAssetBindService;
    private final EdsIdentityFacade edsIdentityFacade;

    @Pointcut(value = "@annotation(com.baiyi.cratos.annotation.PostImportProcessor)")
    public void annotationPoint() {
    }

    @AfterReturning(value = "@annotation(postImportProcessor)", returning = "businessObject")
    public void doAfterReturning(JoinPoint joinPoint, PostImportProcessor postImportProcessor, Object businessObject) {
        if (businessObject instanceof ToBusinessTarget toBusinessTarget) {
            // 绑定资产
            postImportAssetBinding(joinPoint, toBusinessTarget);
            // 按类型处理
            postByTypeProcessor(joinPoint, postImportProcessor.ofType(), toBusinessTarget);
        }
    }

    private void postByTypeProcessor(JoinPoint joinPoint, BusinessTypeEnum businessTypeEnum,
                                     ToBusinessTarget toBusinessTarget) {
        Arrays.stream(joinPoint.getArgs())
                .map(arg -> (HasImportFromAsset) arg)
                .forEach(importFromAsset -> {
                    Integer assetId = importFromAsset.getFromAssetId();
                    IdentityUtils.validIdentityRun(assetId)
                            .withTrue(() -> {
                                try {
                                    Map<String, Object> context = Maps.newHashMap();
                                    context.put("PASSWORD", PasswordGenerator.generatePassword());
                                    EdsAsset asset = edsAssetService.getById(assetId);
                                    PostImportAssetProcessorFactory.process(
                                            SimpleBusiness.builder()
                                                    .businessType(businessTypeEnum.name())
                                                    .businessId(toBusinessTarget.getId())
                                                    .build(), asset, context
                                    );
                                } catch (Exception e) {
                                    log.error(e.getMessage());
                                }
                            });
                });
    }

    private void postImportAssetBinding(JoinPoint joinPoint, ToBusinessTarget toBusinessTarget) {
        Arrays.stream(joinPoint.getArgs())
                .map(arg -> (HasImportFromAsset) arg)
                .forEach(importFromAsset -> {
                    Integer assetId = importFromAsset.getFromAssetId();
                    IdentityUtils.validIdentityRun(assetId)
                            .withTrue(() -> assetBinding(assetId, importFromAsset, toBusinessTarget));
                });
    }

    private void assetBinding(Integer assetId, HasImportFromAsset importFromAsset, ToBusinessTarget toBusinessTarget) {
        int businessId = toBusinessTarget.getId();
        EdsAsset asset = edsAssetService.getById(assetId);
        if (asset == null) {
            // 资产不存在
            return;
        }
        BusinessAssetBind businessAssetBind = BusinessAssetBind.builder()
                .assetId(assetId)
                .businessType(importFromAsset.getBusinessType())
                .businessId(businessId)
                .assetType(asset.getAssetType())
                .build();
        businessAssetBindService.add(businessAssetBind);
    }

}
