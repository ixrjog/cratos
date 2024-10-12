package com.baiyi.cratos.domain.view.crystal;

import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.server.ServerVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/10/11 10:31
 * &#064;Version 1.0
 */
public class CrystalServerVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AssetServer extends BaseVO implements ServerVO.HasServer, Serializable {
        @Serial
        private static final long serialVersionUID = -4491272386847277118L;
        private EdsAssetVO.Asset asset;

        private String name;
        private String instanceId;
        private String remoteMgmtIp;

        public AssetServer init() {
            if (this.asset != null) {
                this.name = asset.getName();
                this.instanceId = asset.getAssetId();
                this.remoteMgmtIp = asset.getAssetKey();
            }
            return this;
        }
    }

}
