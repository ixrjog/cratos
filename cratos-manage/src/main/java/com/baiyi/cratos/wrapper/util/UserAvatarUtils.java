package com.baiyi.cratos.wrapper.util;

import com.baiyi.cratos.common.configuration.CachingConfiguration;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.util.SpringContextUtil;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.domain.view.user.UserVO;
import com.baiyi.cratos.facade.identity.extension.impl.EdsDingtalkIdentityExtensionImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/27 15:40
 * &#064;Version 1.0
 */
@Component
public class UserAvatarUtils {

    @Cacheable(cacheNames = CachingConfiguration.RepositoryName.SHORT_TERM, key = "'USER:AVATAR:USERNAME:' + #username", unless = "#result == null")
    public UserVO.UserAvatar queryUserAvatar(String username) {
        EdsIdentityParam.QueryDingtalkIdentityDetails query = EdsIdentityParam.QueryDingtalkIdentityDetails.builder()
                .username(username)
                .build();
           EdsIdentityVO.DingtalkIdentityDetails details = SpringContextUtil.getBean(
                        EdsDingtalkIdentityExtensionImpl.class)
                .queryDingtalkIdentityDetails(query);

        if (CollectionUtils.isEmpty(details.getDingtalkIdentities())) {
            return UserVO.UserAvatar.NO_DATA;
        }
        return UserVO.UserAvatar.builder()
                .url(details.getDingtalkIdentities()
                        .getFirst()
                        .getAvatar())
                .source("Dingtalk")
                .build();
    }

}
