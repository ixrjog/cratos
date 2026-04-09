package com.baiyi.cratos.eds.network;

import com.baiyi.cratos.eds.core.EdsAssetTypeOfAnnotate;
import com.baiyi.cratos.eds.network.model.NetworkModel;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/9 16:08
 * &#064;Version 1.0
 */
public interface NetworkAllocator extends EdsAssetTypeOfAnnotate, InitializingBean {

    List<NetworkModel.Allocation> queryNetworkAllocations(int instanceId);

    @Override
    default void afterPropertiesSet() {
        NetworkAllocatorFactory.register(this);
    }

}
