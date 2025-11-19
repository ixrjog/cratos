package com.baiyi.cratos.eds.computer;

import com.baiyi.cratos.common.exception.CloudComputerOperationException;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.baiyi.cratos.common.exception.CloudComputerOperationException.NOT_SUPPORTED;
import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/19 11:02
 * &#064;Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class CloudComputerOperatorFactory {

    private static final Map<EdsAssetTypeEnum, HasCloudComputerOperator<?, ?>> CONTEXT = new ConcurrentHashMap<>();

    public static <Config extends IEdsConfigModel, Computer> void register(
            HasCloudComputerOperator<Config, Computer> operatorBean) {
        CONTEXT.put(EdsAssetTypeEnum.valueOf(operatorBean.getAssetType()), operatorBean);
    }

    public static void reboot(String assetType, Integer assetId) throws CloudComputerOperationException {
        EdsAssetTypeEnum assetTypeEnum = EdsAssetTypeEnum.valueOf(assetType);
        if (!CONTEXT.containsKey(assetTypeEnum)) {
            CloudComputerOperationException.runtime(NOT_SUPPORTED);
        }
        CONTEXT.get(assetTypeEnum)
                .reboot(assetId);
    }

    public static void start(String assetType, Integer assetId) throws CloudComputerOperationException {
        EdsAssetTypeEnum assetTypeEnum = EdsAssetTypeEnum.valueOf(assetType);
        if (!CONTEXT.containsKey(assetTypeEnum)) {
            CloudComputerOperationException.runtime(NOT_SUPPORTED);
        }
        CONTEXT.get(assetTypeEnum)
                .start(assetId);
    }

    public static void stop(String assetType, Integer assetId) throws CloudComputerOperationException {
        EdsAssetTypeEnum assetTypeEnum = EdsAssetTypeEnum.valueOf(assetType);
        if (!CONTEXT.containsKey(assetTypeEnum)) {
            CloudComputerOperationException.runtime(NOT_SUPPORTED);
        }
        CONTEXT.get(assetTypeEnum)
                .stop(assetId);
    }

}
