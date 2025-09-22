package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.RbacResource;
import com.baiyi.cratos.domain.view.rbac.RbacResourceVO;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/2/27 11:37
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RbacResourceWrapper extends BaseDataTableConverter<RbacResourceVO.Resource, RbacResource> implements IBaseWrapper<RbacResourceVO.Resource> {

    @Override
    @BusinessWrapper(types = {BusinessTypeEnum.RBAC_GROUP})
    public void wrap(RbacResourceVO.Resource vo) {
    }

}
