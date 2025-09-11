package com.baiyi.cratos.shell.util;

import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.domain.view.doc.BusinessDocVO;

import java.util.ArrayList;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/11 14:51
 * &#064;Version 1.0
 */
public class MyLineMarkdownUtils {

    public static String of(BusinessDocVO.BusinessTextDoc doc) {
        if (doc.getText() == null || doc.getText()
                .trim()
                .isEmpty()) {
            return "";
        }
        String[] lines = doc.getText()
                .split("\n");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            // 检查是否是表格头部（包含|）
            if (line.contains("|") && !line.matches("^\\s*\\|\\s*[-:]+\\s*\\|.*")) {
                List<String> headers = parseTableRow(line);
                if (headers.size() > 1) {
                    // 跳过分隔符行
                    if (i + 1 < lines.length && lines[i + 1].matches(".*\\|.*[-:]+.*\\|.*")) {
                        i++; // 跳过分隔符行
                    }

                    PrettyTable table = PrettyTable.fieldNames(headers.toArray(new String[0]));

                    // 读取表格数据行
                    i++;
                    while (i < lines.length && lines[i].trim()
                            .contains("|")) {
                        List<String> row = parseTableRow(lines[i].trim());
                        if (row.size() == headers.size()) {
                            table.addRow(row.toArray());
                        }
                        i++;
                    }
                    i--; // 回退一行
                    result.append(table)
                            .append("\n");
                } else {
                    result.append(lines[i]).append("\n");
                }
            } else {
                result.append(lines[i]).append("\n");
            }
        }

        return result.toString();
    }

    private static List<String> parseTableRow(String line) {
        List<String> cells = new ArrayList<>();
        String[] parts = line.split("\\|");

        for (String part : parts) {
            String cell = part.trim();
            if (!cell.isEmpty()) {
                cells.add(cell);
            }
        }
        return cells;
    }

}
