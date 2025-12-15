package com.baiyi.cratos.eds.gitlab.event;


import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.http.event.GitLabEventParam;
import com.baiyi.cratos.eds.core.config.model.EdsGitLabConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.gitlab.event.enums.GitLabEventName;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Async;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/10/29 10:54 上午
 * @Version 1.0
 */
@Slf4j
@AllArgsConstructor
public abstract class BaseGitLabEventConsumer<T> implements GitLabEventConsumer, InitializingBean {

    protected final EdsInstanceProviderHolderBuilder holderBuilder;
    protected final EdsAssetIndexService edsAssetIndexService;
    protected final EdsAssetService edsAssetService;

    /**
     * 获取事件枚举
     *
     * @return
     */
    protected abstract GitLabEventName[] getEventNameList();

    @Override
    public List<String> getEventNames() {
        return Arrays.stream(getEventNameList())
                .map(e -> e.name()
                        .toLowerCase())
                .collect(Collectors.toList());
    }

    @Override
    @Async
    @SuppressWarnings("unchecked")
    public void consumeEventV4(EdsInstance instance, GitLabEventParam.SystemHook systemHook) {
        EdsInstanceProviderHolder<EdsGitLabConfigModel.GitLab, GitLabEventParam.SystemHook> holder = (EdsInstanceProviderHolder<EdsGitLabConfigModel.GitLab, GitLabEventParam.SystemHook>) holderBuilder.newHolder(
                instance.getId(), EdsAssetTypeEnum.GITLAB_SYSTEM_HOOK.name());
        // 导入资产
        EdsAsset asset = holder.importAsset(systemHook);
        postProcess(instance, asset, systemHook);
    }

    abstract protected void postProcess(EdsInstance instance, EdsAsset asset, GitLabEventParam.SystemHook systemHook);

    @SuppressWarnings("unchecked")
    protected EdsInstanceProviderHolder<EdsGitLabConfigModel.GitLab, T> getHolder(EdsInstance instance) {
        return (EdsInstanceProviderHolder<EdsGitLabConfigModel.GitLab, T>) holderBuilder.newHolder(
                instance.getId(), getAssetType());
    }

    /**
     * 资产类型
     *
     * @return
     */
    protected abstract String getAssetType();

    @Override
    public void afterPropertiesSet() throws Exception {
        GitLabEventConsumerFactory.register(this);
    }

}
