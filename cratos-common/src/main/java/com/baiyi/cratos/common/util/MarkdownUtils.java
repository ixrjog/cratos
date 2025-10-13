package com.baiyi.cratos.common.util;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.text.TextContentRenderer;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/14 15:04
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MarkdownUtils {

    public static String removeMarkdownTags(String markdown) {
        Parser parser = Parser.builder()
                .build();
        Node document = parser.parse(markdown);
        TextContentRenderer renderer = TextContentRenderer.builder()
                .build();
        return renderer.render(document);
    }

    public static String generateMarkdownSeparator(String header) {
        // 计算表头中的字段数量（通过 | 分隔符计算）
        // 减去首尾的空字符串
        int fieldCount = header.split("\\|").length - 2;
        // 生成分隔符行
        StringBuilder separator = new StringBuilder("|");
        for (int i = 0; i < fieldCount; i++) {
            separator.append(" --- |");
        }
        return separator.toString();
    }

    public static String generateMarkdownTableRow(Object... columns) {
        StringBuilder row = new StringBuilder("|");
        for (Object column : columns) {
            row.append(" ")
                    .append(column)
                    .append(" |");
        }
        return row.toString();
    }

}
