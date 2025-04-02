package com.baiyi.cratos.eds.core.comparer;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.util.AssetUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

/**
 * @Author baiyi
 * @Date 2023/12/25 17:01
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EdsAssetComparer {

    private EdsAsset a1;
    private EdsAsset a2;

    public static EdsAssetComparer newBuilder() {
        return new EdsAssetComparer();
    }

    public EdsAssetComparer withAsset1(EdsAsset a1) {
        this.a1 = a1;
        return this;
    }

    public EdsAssetComparer withAsset2(EdsAsset a2) {
        this.a2 = a2;
        return this;
    }

    public static final EdsAssetComparer SAME = EdsAssetComparer.builder()
            .equal(true)
            .build();

    public static final boolean DIFFERENT = false;

    public static final EdsAssetComparer COMPARE_NAME = EdsAssetComparer.builder()
            .comparisonOfName(true)
            .build();

    public static final EdsAssetComparer COMPARE_DESCRIPTION = EdsAssetComparer.builder()
            .comparisonOfDescription(true)
            .build();

    @Builder.Default
    private boolean comparisonOfName = false;

    @Builder.Default
    private boolean comparisonOfAssetId = false;

    @Builder.Default
    private boolean comparisonOfKind = false;

    @Builder.Default
    private boolean comparisonOfKey = false;

    @Builder.Default
    private boolean comparisonOfExpiredTime = false;

    @Builder.Default
    private boolean comparisonOfDescription = false;

    @Builder.Default
    private boolean comparisonOfValid = false;

    @Builder.Default
    private boolean comparisonOfCreatedTime = false;

    @Builder.Default
    public boolean comparisonOfOriginalModel = false;

    @Builder.Default
    private boolean equal = false;

    public boolean compare() {
        if (this.isEqual()) {
            return true;
        }
        if (this.isComparisonOfName() && !AssetUtils.equals(a2.getName(), a1.getName())) {
            return false;
        }
        if (this.isComparisonOfAssetId() && !AssetUtils.equals(a2.getAssetId(), a1.getAssetId())) {
            return false;
        }
        if (this.isComparisonOfKey() && !AssetUtils.equals(a2.getAssetKey(), a1.getAssetKey())) {
            return false;
        }
        if (this.isComparisonOfDescription() && !AssetUtils.equals(a2.getDescription(), a1.getDescription())) {
            return false;
        }
        if (this.isComparisonOfKind() && !AssetUtils.equals(a2.getKind(), a1.getKind())) {
            return false;
        }
        if (this.isComparisonOfExpiredTime() && !AssetUtils.equals(a2.getExpiredTime(), a1.getExpiredTime())) {
            return false;
        }
        if (this.isComparisonOfCreatedTime() && a2.getCreatedTime() != a1.getCreatedTime()) {
            return false;
        }
        if (this.isComparisonOfValid()) {
            return a2.getValid()
                    .equals(a1.getValid());
        }
        if (this.isComparisonOfOriginalModel()) {
            return StringUtils.hasText(a1.getOriginalModel()) && a1.getOriginalModel()
                    .equals(a2.getOriginalModel());
        }
        return true;
    }

}