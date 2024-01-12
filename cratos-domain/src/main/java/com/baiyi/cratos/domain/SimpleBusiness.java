package com.baiyi.cratos.domain;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2024/1/12 14:58
 * @Version 1.0
 */
@Data
@Builder
public class SimpleBusiness implements BaseBusiness.IBusiness {

    private String businessType;

    private Integer businessId;

}
