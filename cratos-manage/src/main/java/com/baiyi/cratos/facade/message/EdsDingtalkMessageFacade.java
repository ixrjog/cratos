package com.baiyi.cratos.facade.message;

import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.core.config.EdsDingtalkConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.dingtalk.param.DingtalkMessageParam;
import com.baiyi.cratos.eds.dingtalk.sender.DingtalkMessageSender;
import com.baiyi.cratos.facade.EdsFacade;
import com.baiyi.cratos.facade.message.builder.AsyncSendMessageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/24 17:39
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class EdsDingtalkMessageFacade {

    private final DingtalkMessageSender dingtalkMessageSender;
    private final EdsFacade edsFacade;
    private final EdsInstanceProviderHolderBuilder holderBuilder;

    public void sendToDingtalkUser(User sendToUser, NotificationTemplate notificationTemplate, String msgText) {
        EdsInstanceParam.QueryDingtalkIdentityDetails query = EdsInstanceParam.QueryDingtalkIdentityDetails.builder()
                .username(sendToUser.getUsername())
                .build();
        EdsAssetVO.DingtalkIdentityDetails dingtalkIdentityDetails = edsFacade.queryDingtalkIdentityDetails(query);
        if (CollectionUtils.isEmpty(dingtalkIdentityDetails.getDingtalkIdentities())) {
            // 用户没有关联Dingtalk身份
            return;
        }
        dingtalkIdentityDetails.getDingtalkIdentities()
                .forEach((assetId, dingtalkUserAsset) -> sendToDingtalkUser(dingtalkUserAsset, notificationTemplate, msgText));
    }

    @SuppressWarnings("unchecked")
    private void sendToDingtalkUser(EdsAssetVO.Asset dingtalkUserAsset, NotificationTemplate notificationTemplate, String msgText) {
        EdsInstanceProviderHolder<EdsDingtalkConfigModel.Dingtalk, ?> holder = (EdsInstanceProviderHolder<EdsDingtalkConfigModel.Dingtalk, ?>) holderBuilder.newHolder(
                dingtalkUserAsset.getInstanceId(), EdsAssetTypeEnum.DINGTALK_USER.name());
        EdsDingtalkConfigModel.Dingtalk dingtalk = holder.getInstance()
                .getEdsConfigModel();
        DingtalkMessageParam.AsyncSendMessage asyncSendMessage = AsyncSendMessageBuilder.newBuilder()
                .withMsgText(msgText)
                .withNotificationTemplate(notificationTemplate)
                .withUserId(dingtalkUserAsset.getAssetId())
                .build();
        dingtalkMessageSender.asyncSend(dingtalk, asyncSendMessage);
    }

}
