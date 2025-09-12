package com.baiyi.cratos.shell.util;

import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.domain.view.doc.BusinessDocVO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/11 14:51
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MyLineMarkdownUtils {

    public static String of(BusinessDocVO.BusinessTextDoc doc) {
        String text = doc.getText();
        if (text == null || text.trim()
                .isEmpty()) {
            return "";
        }
        String[] lines = text.split("\n");
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (i < lines.length) {
            String line = lines[i].trim();
            // 检查是否是表格头部（包含|）
            if (line.contains("|") && !line.matches("^\\s*\\|\\s*[-:]+\\s*\\|.*")) {
                List<String> headers = parseTableRow(line);
                if (headers.size() > 1) {
                    // 跳过分隔符行
                    if (i + 1 < lines.length && lines[i + 1].matches(".*\\|.*[-:]+.*\\|.*")) {
                        i++;
                    }
                    PrettyTable table = PrettyTable.fieldNames(headers.toArray(new String[0]));
                    i++;
                    while (i < lines.length) {
                        String dataLine = lines[i].trim();
                        if (!dataLine.contains("|")) break;
                        List<String> row = parseTableRow(dataLine);
                        if (row.size() == headers.size()) {
                            table.addRow(row.toArray());
                        }
                        i++;
                    }
                    result.append(table)
                            .append("\n");
                    continue;
                }
            }
            result.append(lines[i])
                    .append("\n");
            i++;
        }
        return result.toString();
    }

    private static List<String> parseTableRow(String line) {
        String[] parts = line.split("\\|", -1);
        int start = 0, end = parts.length;
        if (parts.length > 0 && parts[0].trim()
                .isEmpty()) start++;
        if (parts.length > 1 && parts[parts.length - 1].trim()
                .isEmpty()) end--;
        List<String> cells = new ArrayList<>(end - start);
        for (int i = start; i < end; i++) {
            cells.add(parts[i].trim());
        }
        return cells;
    }

}
