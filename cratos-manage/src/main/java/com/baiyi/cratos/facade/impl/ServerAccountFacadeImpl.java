package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.enums.RemoteManagementProtocolEnum;
import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.ServerAccount;
import com.baiyi.cratos.domain.param.server.ServerAccountParam;
import com.baiyi.cratos.domain.view.server.ServerAccountVO;
import com.baiyi.cratos.facade.ServerAccountFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.ServerAccountService;
import com.baiyi.cratos.wrapper.ServerAccountWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/4/15 上午11:06
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ServerAccountFacadeImpl implements ServerAccountFacade {

    private final CredentialService credService;

    private final ServerAccountService accountService;

    private final ServerAccountWrapper serverAccountWrapper;

    private static final String ROOT = "root";

    private static final String ADMIN = "Administrators";

    @Override
    public void addServerAccount(ServerAccountParam.AddServerAccount addServerAccount) {
        ServerAccount serverAccount = addServerAccount.toTarget();
        saveServerAccount(serverAccount);
    }

    @Override
    public void updateServerAccount(ServerAccountParam.UpdateServerAccount updateServerAccount) {
        ServerAccount serverAccount = updateServerAccount.toTarget();
        saveServerAccount(serverAccount);
    }

    @Override
    public void setServerAccountValidById(int id) {
        accountService.updateValidById(id);
    }

    @Override
    public void deleteById(int id) {
        // TODO
        //  accountService.deleteById(id);
    }

    @Override
    public DataTable<ServerAccountVO.ServerAccount> queryServerAccountPage(
            ServerAccountParam.ServerAccountPageQuery pageQuery) {
        DataTable<ServerAccount> table = accountService.queryServerAccountPage(pageQuery);
        return serverAccountWrapper.wrapToTarget(table);
    }

    private void saveServerAccount(ServerAccount serverAccount) {
        if (ROOT.equals(serverAccount.getUsername())) {
            serverAccount.setSudo(true);
        }
        if (RemoteManagementProtocolEnum.RDP.name()
                .equals(serverAccount.getProtocol()) && ADMIN.equalsIgnoreCase(serverAccount.getUsername())) {
            serverAccount.setSudo(true);
        }
        if (IdentityUtil.hasIdentity(serverAccount.getCredentialId())) {
            serverAccount.setValid(credService.getById(serverAccount.getCredentialId()) != null);
        } else {
            serverAccount.setValid(false);
        }
        if (serverAccount.getId() == null) {
            accountService.add(serverAccount);
        } else {
            accountService.updateByPrimaryKey(serverAccount);
        }
    }

}
