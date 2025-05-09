package com.baiyi.cratos.eds.aws.model;

import com.amazonaws.services.transfer.model.ListedServer;
import com.amazonaws.services.transfer.model.ListedUser;
import com.baiyi.cratos.eds.core.config.base.HasRegionId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/13 下午2:09
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AwsTransferServer {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TransferServer implements HasRegionId {
        private String regionId;
        private ListedServer server;
        private List<ListedUser> users;
    }

}
