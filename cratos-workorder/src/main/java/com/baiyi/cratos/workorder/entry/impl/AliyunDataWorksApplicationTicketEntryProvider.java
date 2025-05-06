package com.baiyi.cratos.workorder.entry.impl;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/6 11:33
 * &#064;Version 1.0
 */
@Component
@BusinessType(type = BusinessTypeEnum.EDS_INSTANCE)
@WorkOrderKey(key = WorkOrderKeys.ALIYUN_DATAWORKS_AK)
public class AliyunDataWorksApplicationTicketEntryProvider {
}
