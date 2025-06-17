package com.baiyi.cratos.facade.fin;

import com.baiyi.cratos.domain.param.http.finops.FinOpsParam;
import com.baiyi.cratos.domain.view.finops.FinOpsVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/16 16:07
 * &#064;Version 1.0
 */
public interface FinOpsFacade {

    FinOpsVO.AppCost queryAppCost(FinOpsParam.QueryAppCost queryAppCost);

}
