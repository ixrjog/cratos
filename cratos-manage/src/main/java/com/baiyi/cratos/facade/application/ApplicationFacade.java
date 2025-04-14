package com.baiyi.cratos.facade.application;

import com.baiyi.cratos.HasSetValid;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.application.ApplicationParam;
import com.baiyi.cratos.domain.view.application.ApplicationVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/15 11:15
 * &#064;Version 1.0
 */
public interface ApplicationFacade extends HasSetValid {

    DataTable<ApplicationVO.Application> queryApplicationPage(ApplicationParam.ApplicationPageQuery pageQuery);

    ApplicationVO.Application getApplicationByName(ApplicationParam.GetApplication getApplication);

    void addApplication(ApplicationParam.AddApplication addApplication);

    void updateApplication(ApplicationParam.UpdateApplication updateApplication);

    void deleteById(int id);

    void scanApplicationResource(ApplicationParam.ScanResource scanResource);

    void scanAllApplicationResource();

}
