package com.baiyi.cratos.ssh.crystal.context;

import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.eds.core.EdsInstanceQueryHelper;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkService;
import com.baiyi.cratos.service.*;
import com.baiyi.cratos.ssh.core.config.SshAuditProperties;
import com.baiyi.cratos.ssh.core.facade.HostSystemFacade;
import com.baiyi.cratos.ssh.core.facade.SimpleSshSessionFacade;
import com.baiyi.cratos.ssh.core.proxy.SshProxyHostHolder;
import com.baiyi.cratos.ssh.crystal.access.ServerAccessControlFacade;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/23 09:51
 * &#064;Version 1.0
 */
@Getter
@Component
@RequiredArgsConstructor
public class SshCrystalOpenMessageHandlerContext {

    private final EdsAssetService edsAssetService;
    private final ServerAccountService serverAccountService;
    private final CredentialService credentialService;
    private final ServerAccessControlFacade serverAccessControlFacade;
    private final BusinessTagFacade businessTagFacade;
    private final UserService userService;
    private final NotificationTemplateService notificationTemplateService;
    private final EdsInstanceQueryHelper edsInstanceQueryHelper;
    private final EdsConfigService edsConfigService;
    private final DingtalkService dingtalkService;
    private final SshAuditProperties sshAuditProperties;
    private final SimpleSshSessionFacade simpleSshSessionFacade;
    private final SshProxyHostHolder proxyHostHolder;
    private final HostSystemFacade hostSystemFacade;

}
