package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.application.ApplicationParam;
import com.baiyi.cratos.domain.view.application.ApplicationVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/15 11:15
 * &#064;Version 1.0
 */
public interface ApplicationFacade {

    DataTable<ApplicationVO.Application> queryApplicationPage(ApplicationParam.ApplicationPageQuery pageQuery);

}
