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

}
