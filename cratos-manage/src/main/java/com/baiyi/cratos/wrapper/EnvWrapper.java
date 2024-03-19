package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.domain.generator.Env;
import com.baiyi.cratos.domain.view.env.EnvVO;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/3/19 14:34
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class  EnvWrapper extends BaseDataTableConverter<EnvVO.Env, Env> implements IBaseWrapper<EnvVO.Env> {

    @Override
    public void wrap(EnvVO.Env env) {

    }

}
