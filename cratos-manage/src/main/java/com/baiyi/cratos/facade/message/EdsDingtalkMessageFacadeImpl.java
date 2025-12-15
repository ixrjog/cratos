package com.baiyi.cratos.facade.message;

import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.facade.EdsDingtalkMessageFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.dingtalk.sender.DingtalkMessageSender;
import com.baiyi.cratos.eds.core.facade.EdsIdentityFacade;
import com.baiyi.cratos.facade.message.builder.AsyncSendMessageAgency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/24 17:39
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EdsDingtalkMessageFacadeImpl implements EdsDingtalkMessageFacade {

    private final DingtalkMessageSender dingtalkMessageSender;
    private final EdsIdentityFacade edsIdentityFacade;
    private final EdsInstanceProviderHolderBuilder holderBuilder;

    @Override
    public void sendToDingtalkUser(User sendToUser, NotificationTemplate notificationTemplate, String msgText) {
        EdsIdentityParam.QueryDingtalkIdentityDetails query = EdsIdentityParam.QueryDingtalkIdentityDetails.builder()
                .username(sendToUser.getUsername())
                .build();
        EdsIdentityVO.DingtalkIdentityDetails dingtalkIdentityDetails = edsIdentityFacade.queryDingtalkIdentityDetails(
                query);
        if (CollectionUtils.isEmpty(dingtalkIdentityDetails.getDingtalkIdentities())) {
            // 用户没有关联Dingtalk身份
            log.debug("The user {} is not associated with Dingtalk identity.", sendToUser.getUsername());
            return;
        }
        dingtalkIdentityDetails.getDingtalkIdentities()
                .forEach(identity -> sendToDingtalkUser(identity, notificationTemplate, msgText));
    }

    @SuppressWarnings("unchecked")
    private void sendToDingtalkUser(EdsIdentityVO.DingtalkIdentity dingtalkIdentity,
                                    NotificationTemplate notificationTemplate, String msgText) {
        EdsInstanceProviderHolder<EdsConfigs.Dingtalk, ?> holder = (EdsInstanceProviderHolder<EdsConfigs.Dingtalk, ?>) holderBuilder.newHolder(
                dingtalkIdentity.getInstance()
                        .getId(), EdsAssetTypeEnum.DINGTALK_USER.name());
        EdsConfigs.Dingtalk dingtalk = holder.getInstance()
                .getConfig();
        AsyncSendMessageAgency.newBuilder()
                .withMsgText(msgText)
                .withNotificationTemplate(notificationTemplate)
                .withUserId(dingtalkIdentity.getAccount()
                        .getAssetId())
                .withDingtalkMessageSender(dingtalkMessageSender)
                .withDingtalk(dingtalk)
                .asyncSend();
    }

}
