package com.baiyi.cratos.eds.acme;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.eds.acme.enums.AcmeProviderEnum;
import com.baiyi.cratos.eds.acme.manager.SecureAcmeAccountManager;
import com.baiyi.cratos.eds.acme.model.AcmeModel;
import org.junit.jupiter.api.Test;
import org.shredzone.acme4j.exception.AcmeException;

import java.io.IOException;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/9 17:33
 * &#064;Version 1.0
 */
public class SecureAcmeAccountManagerTest extends BaseUnit {

    @Test
    void test1() {
        try {
            AcmeModel.Account acmeAccount = SecureAcmeAccountManager.createAccount(
                    "jan.liang@palmpay-inc.com", AcmeProviderEnum.LETSENCRYPT_STAGING);
            System.out.println(acmeAccount);
        } catch (AcmeException acmeException) {
            acmeException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

}
