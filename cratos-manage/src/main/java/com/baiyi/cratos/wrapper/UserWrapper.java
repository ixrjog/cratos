package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.annotation.Sensitive;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.view.user.UserVO;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/10 10:29
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserWrapper extends BaseDataTableConverter<UserVO.User, User> implements IBaseWrapper<UserVO.User> {

    @Override
    @BusinessWrapper(types = {BusinessTypeEnum.BUSINESS_TAG})
    @Sensitive
    public void wrap(UserVO.User user) {
        // This is a good idea
    }

}
