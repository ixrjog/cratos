package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Env;
import com.baiyi.cratos.domain.view.env.EnvVO;
import com.baiyi.cratos.service.EnvService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBusinessWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @Author baiyi
 * @Date 2024/3/19 14:34
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.ENV)
public class EnvWrapper extends BaseDataTableConverter<EnvVO.Env, Env> implements IBusinessWrapper<EnvVO.HasEnv, EnvVO.Env> {

    private final EnvService envService;

    @Override
    @BusinessWrapper(types = {BusinessTypeEnum.BUSINESS_TAG})
    public void wrap(EnvVO.Env vo) {
    }

    @Override
    public void businessWrap(EnvVO.HasEnv hasEnv) {
        if (!StringUtils.hasText(hasEnv.getEnvName())) {
            return;
        }
        Env uniqueKey = Env.builder()
                .envName(hasEnv.getEnvName())
                .build();
        Env env = envService.getByUniqueKey(uniqueKey);
        if (env == null) {
            return;
        }
        EnvVO.Env envVO = this.convert(env);
        wrapFromProxy(envVO);
        hasEnv.setEnv(envVO);
    }

}
