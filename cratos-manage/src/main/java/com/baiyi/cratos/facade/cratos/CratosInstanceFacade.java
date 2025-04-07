package com.baiyi.cratos.facade.cratos;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.cratos.CratosInstanceParam;
import com.baiyi.cratos.domain.view.cratos.CratosInstanceVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/7 10:50
 * &#064;Version 1.0
 */
public interface CratosInstanceFacade {

    DataTable<CratosInstanceVO.RegisteredInstance> queryRegisteredInstancePage(
            CratosInstanceParam.RegisteredInstancePageQuery pageQuery);

    CratosInstanceVO.Health checkHealth();

    void setValidById(int id);

    void deleteById(int id);

}
