package com.baiyi.cratos.eds.core.comparer;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.util.AssetUtil;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2023/12/25 17:01
 * @Version 1.0
 */
@Data
@Builder
public class EdsAssetComparer {

    public static final EdsAssetComparer SAME = EdsAssetComparer.builder()
            .equal(true)
            .build();

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
    private boolean equal = false;

    public boolean compare(EdsAsset a1, EdsAsset a2) {
        // 相同
        if (this.isEqual()) {
            return true;
        }
        if (this.isComparisonOfName()) {
            if (!AssetUtil.equals(a2.getName(), a1.getName())) {
                return false;
            }
        }
        if (this.isComparisonOfAssetId()) {
            if (!AssetUtil.equals(a2.getAssetId(), a1.getAssetId())) {
                return false;
            }
        }
        if (this.isComparisonOfKey()) {
            if (!AssetUtil.equals(a2.getAssetKey(), a1.getAssetKey())) {
                return false;
            }
        }
        if (this.isComparisonOfDescription()) {
            if (!AssetUtil.equals(a2.getDescription(), a1.getDescription())) {
                return false;
            }
        }
        if (this.isComparisonOfKind()) {
            if (!AssetUtil.equals(a2.getKind(), a1.getKind())) {
                return false;
            }
        }
        if (this.isComparisonOfExpiredTime()) {
            if (!AssetUtil.equals(a2.getExpiredTime(), a1.getExpiredTime())) {
                return false;
            }
        }
        if (this.isComparisonOfCreatedTime()) {
            if (a2.getCreatedTime() != a1.getCreatedTime()) {
                return false;
            }
        }
        if (this.isComparisonOfValid()) {
            return a2.getValid()
                    .equals(a1.getValid());
        }
        return true;
    }

}