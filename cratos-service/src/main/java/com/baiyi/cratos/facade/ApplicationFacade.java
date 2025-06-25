package com.baiyi.cratos.facade;

import com.baiyi.cratos.HasSetValid;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.model.ApplicationModel;
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

    Application createApplication(ApplicationModel.CreateFrontEndApplication createFrontEndApplication);

    void deleteById(int id);

    void scanApplicationResource(ApplicationParam.ScanResource scanResource);

    void scanAllApplicationResource();

}
