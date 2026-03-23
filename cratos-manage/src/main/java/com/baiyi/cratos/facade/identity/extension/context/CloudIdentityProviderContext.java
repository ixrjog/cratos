package com.baiyi.cratos.facade.identity.extension.context;

import com.baiyi.cratos.eds.core.holder.EdsProviderHolderFactory;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.wrapper.EdsAssetWrapper;
import com.baiyi.cratos.wrapper.EdsInstanceWrapper;
import com.baiyi.cratos.wrapper.UserWrapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/20 16:12
 * &#064;Version 1.0
 */
@Getter
@Component
@RequiredArgsConstructor
public class CloudIdentityProviderContext {

    private final EdsAssetService edsAssetService;
    private final EdsAssetWrapper edsAssetWrapper;
    private final EdsAssetIndexService edsAssetIndexService;
    private final UserService userService;
    private final UserWrapper userWrapper;
    private final EdsInstanceWrapper instanceWrapper;
    private final EdsProviderHolderFactory edsProviderHolderFactory;

}
