package com.baiyi.cratos.facade.application;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.application.ApplicationResourceBaselineParam;
import com.baiyi.cratos.domain.view.application.ApplicationResourceBaselineVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/30 11:00
 * &#064;Version 1.0
 */
public interface ApplicationResourceBaselineFacade {

    void scanAll();

    void rescan(int baselineId);

    void mergeToBaseline(int baselineId);

    DataTable<ApplicationResourceBaselineVO.ResourceBaseline> queryApplicationResourceBaselinePage(
            ApplicationResourceBaselineParam.ApplicationResourceBaselinePageQuery pageQuery);

    void redeploy(int baselineId);

}
