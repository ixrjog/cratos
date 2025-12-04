package com.baiyi.cratos.common.util;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.text.TextContentRenderer;

/**
 * @Author baiyi
 * @Date 2025/3/14 15:04
 * @Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MarkdownUtils {

    public static String toPlainText(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        TextContentRenderer renderer = TextContentRenderer.builder().build();
        return renderer.render(document);
    }

    public static String createTableHeader(String header) {
        return header + "\n" + createTableSeparator(header) + "\n";
    }

    private static String createTableSeparator(String header) {
        String[] parts = header.split("\\|");
        int fieldCount = 0;
        for (String part : parts) {
            if (!part.trim().isEmpty()) {
                fieldCount++;
            }
        }
        return "|" + " --- |".repeat(Math.max(0, fieldCount));
    }

    public static String createTableRow(Object... columns) {
        StringBuilder row = new StringBuilder("|");
        for (Object column : columns) {
            row.append(" ").append(column).append(" |");
        }
        return row.toString();
    }

}
