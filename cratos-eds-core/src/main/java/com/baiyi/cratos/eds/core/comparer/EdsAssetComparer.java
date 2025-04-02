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

    public static final EdsAssetComparer SAME = EdsAssetComparer.builder()
            .equal(true)
            .build();

    public static final boolean DIFFERENT = false;

    public static final EdsAssetComparer COMPARE_NAME = EdsAssetComparer.builder()
            .comparisonName(true)
            .build();

    public static final EdsAssetComparer COMPARE_DESCRIPTION = EdsAssetComparer.builder()
            .comparisonDescription(true)
            .build();

    @Builder.Default
    private boolean comparisonName = false;

    @Builder.Default
    private boolean comparisonAssetId = false;

    @Builder.Default
    private boolean comparisonKind = false;

    @Builder.Default
    private boolean comparisonKey = false;

    @Builder.Default
    private boolean comparisonExpiredTime = false;

    @Builder.Default
    private boolean comparisonDescription = false;

    @Builder.Default
    private boolean comparisonValid = false;

    @Builder.Default
    private boolean comparisonCreatedTime = false;

    @Builder.Default
    public boolean comparisonOriginalModel = false;

    @Builder.Default
    private boolean equal = false;

    public boolean compare(EdsAsset a1, EdsAsset a2) {
        if (this.isEqual()) {
            return true;
        }
        if (this.isComparisonName() && !AssetUtils.equals(a2.getName(), a1.getName())) {
            return false;
        }
        if (this.isComparisonAssetId() && !AssetUtils.equals(a2.getAssetId(), a1.getAssetId())) {
            return false;
        }
        if (this.isComparisonKey() && !AssetUtils.equals(a2.getAssetKey(), a1.getAssetKey())) {
            return false;
        }
        if (this.isComparisonDescription() && !AssetUtils.equals(a2.getDescription(), a1.getDescription())) {
            return false;
        }
        if (this.isComparisonKind() && !AssetUtils.equals(a2.getKind(), a1.getKind())) {
            return false;
        }
        if (this.isComparisonExpiredTime() && !AssetUtils.equals(a2.getExpiredTime(), a1.getExpiredTime())) {
            return false;
        }
        if (this.isComparisonCreatedTime() && a2.getCreatedTime() != a1.getCreatedTime()) {
            return false;
        }
        if (this.isComparisonValid()) {
            return a2.getValid()
                    .equals(a1.getValid());
        }
        if (this.isComparisonOriginalModel()) {
            return StringUtils.hasText(a1.getOriginalModel()) && a1.getOriginalModel()
                    .equals(a2.getOriginalModel());
        }
        return true;
    }

}