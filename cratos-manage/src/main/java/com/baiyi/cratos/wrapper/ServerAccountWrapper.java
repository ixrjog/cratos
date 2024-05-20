package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.ServerAccount;
import com.baiyi.cratos.domain.view.server.ServerAccountVO;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/20 下午1:59
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ServerAccountWrapper extends BaseDataTableConverter<ServerAccountVO.ServerAccount, ServerAccount> implements IBaseWrapper<ServerAccountVO.ServerAccount> {

    @Override
    @BusinessWrapper(ofTypes = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC})
    public void wrap(ServerAccountVO.ServerAccount serverAccount) {
    }

}