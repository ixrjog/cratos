package com.baiyi.cratos.facade.identity;

import com.baiyi.cratos.facade.identity.extension.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/26 10:19
 * &#064;Version 1.0
 */
public interface EdsIdentityFacade extends EdsCloudIdentityExtension, EdsLdapIdentityExtension, EdsGitLabIdentityExtension, EdsDingtalkIdentityExtension, EdsMailIdentityExtension {

}
