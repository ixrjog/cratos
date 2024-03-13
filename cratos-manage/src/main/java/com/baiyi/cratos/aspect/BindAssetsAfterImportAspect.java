package com.baiyi.cratos.aspect;

import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.domain.generator.BusinessAssetBind;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.param.IImportFromAsset;
import com.baiyi.cratos.domain.view.eds.IToBusinessTarget;
import com.baiyi.cratos.service.BusinessAssetBindService;
import com.baiyi.cratos.service.EdsAssetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @Author baiyi
 * @Date 2024/3/13 11:04
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class BindAssetsAfterImportAspect {

    private final EdsAssetService edsAssetService;

    private final BusinessAssetBindService businessAssetBindService;

    @Pointcut(value = "@annotation(com.baiyi.cratos.annotation.BindAssetsAfterImport)")
    public void annotationPoint() {
    }

    @AfterReturning(value = "annotationPoint()", returning = "businessObject")
    public void doAfterReturning(JoinPoint joinPoint, Object businessObject) {
        Arrays.stream(joinPoint.getArgs())
                .filter(arg -> arg instanceof IImportFromAsset)
                .map(arg -> (IImportFromAsset) arg)
                .forEach(importFromAsset -> {
                    Integer assetId = importFromAsset.getFromAssetId();
                    IdentityUtil.validIdentityRun(assetId)
                            .withTrue(() -> {
                                if (businessObject instanceof IToBusinessTarget toBusinessTarget) {
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
                            });
                });
    }

}
