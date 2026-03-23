package com.baiyi.cratos.facade.identity.extension.context;

import com.baiyi.cratos.eds.core.holder.EdsProviderHolderFactory;
import com.baiyi.cratos.facade.EdsFacade;
import com.baiyi.cratos.service.*;
import com.baiyi.cratos.wrapper.EdsAssetWrapper;
import com.baiyi.cratos.wrapper.EdsInstanceWrapper;
import com.baiyi.cratos.wrapper.UserWrapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/20 15:54
 * &#064;Version 1.0
 */
@Getter
@Component
@RequiredArgsConstructor
public class EdsIdentityExtensionContext {

    private final EdsAssetWrapper edsAssetWrapper;
    private final EdsInstanceService edsInstanceService;
    private final EdsInstanceWrapper edsInstanceWrapper;
    private final UserService userService;
    private final UserWrapper userWrapper;
    private final EdsProviderHolderFactory edsProviderHolderFactory;
    private final EdsAssetService edsAssetService;
    private final EdsFacade edsFacade;
    private final EdsAssetIndexService edsAssetIndexService;
    private final TagService tagService;
    private final BusinessTagService businessTagService;

}
