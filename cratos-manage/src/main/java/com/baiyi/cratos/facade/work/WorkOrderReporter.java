package com.baiyi.cratos.facade.work;

import com.baiyi.cratos.domain.Report;
import com.baiyi.cratos.domain.view.work.WorkOrderReportVO;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/8 13:52
 * &#064;Version 1.0
 */
public interface WorkOrderReporter {

    List<Report.BaseData> getTicketNameReport();

    WorkOrderReportVO.Monthly getTicketMonthlyReport();

}
