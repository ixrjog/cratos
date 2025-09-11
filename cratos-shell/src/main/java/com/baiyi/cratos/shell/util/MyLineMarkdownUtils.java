package com.baiyi.cratos.shell.util;

import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.domain.view.doc.BusinessDocVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/11 14:51
 * &#064;Version 1.0
 */
public class MyLineMarkdownUtils {

    public static String of(BusinessDocVO.BusinessTextDoc doc) {
        PrettyTable computerTable = PrettyTable.fieldNames("ID", "Name", "Text");
        computerTable.addRow(doc.getId(), doc.getName(), doc.getText());
        return computerTable.toString();
    }

}
