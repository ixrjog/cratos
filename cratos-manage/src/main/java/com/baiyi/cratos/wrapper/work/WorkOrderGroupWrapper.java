package com.baiyi.cratos.wrapper.work;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.WorkOrderGroup;
import com.baiyi.cratos.domain.util.I18nUtils;
import com.baiyi.cratos.domain.view.work.WorkOrderVO;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/17 14:21
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.WORKORDER_GROUP)
public class WorkOrderGroupWrapper extends BaseDataTableConverter<WorkOrderVO.Group, WorkOrderGroup> implements IBaseWrapper<WorkOrderVO.Group> {

    @Override
    @BusinessWrapper(ofTypes = {BusinessTypeEnum.WORKORDER})
    public void wrap(WorkOrderVO.Group vo) {
        // This is a good idea
        I18nUtils.setI18nData(vo);
    }

}