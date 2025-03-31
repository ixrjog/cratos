package com.baiyi.cratos.eds.core.facade;

import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.domain.generator.User;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/31 10:33
 * &#064;Version 1.0
 */
public interface EdsDingtalkMessageFacade {

    void sendToDingtalkUser(User sendToUser, NotificationTemplate notificationTemplate, String msgText);

}
