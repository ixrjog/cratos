package com.baiyi.cratos.common.table.converter;


import com.baiyi.cratos.common.table.Bordered;
import com.baiyi.cratos.common.table.Converter;
import com.baiyi.cratos.common.table.PrettyTable;
import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class ConsoleConverter implements Converter, Bordered {

    private boolean border;

    public ConsoleConverter border(final boolean border) {
        this.border = border;
        return this;
    }

    abstract ConsoleConverter clear();

    abstract ConsoleConverter af(String text);

    abstract ConsoleConverter ab(String text);

    @Override
    public String convert(PrettyTable pt) {
        clear();
        // Check empty
        if (pt.fieldNames.isEmpty()) {
            return "";
        }
        int[] maxWidth = adjustMaxWidth(pt);
        topBorderLine(maxWidth);
        leftBorder();
        IntStream.range(0, pt.fieldNames.size())
                .forEach(i -> {
                    String headerText = pt.fieldNames.get(i);
                    // 计算中文字符占用的额外宽度
                    int chineseCompensation = fixLength(headerText);
                    // 调整填充宽度，考虑中文字符
                    af(StringUtils.rightPad(headerText, maxWidth[i] - chineseCompensation));
                    if (i < pt.fieldNames.size() - 1) {
                        centerBorder();
                    } else {
                        rightBorder();
                    }
                });
        bottomBorderLine(maxWidth);
        // Convert rows to table
        for (Object[] r : pt.rows) {
            ab("\n");
            leftBorder();
            int rL = colLength(Arrays.toString(r));
            // for (int c = 0; c < r.length; c++) {
            for (int c = 0; c < r.length; c++) {
                String nc;
                if (r[c] instanceof Number) {
                    // 数字类型，使用左填充（右对齐）
                    String n = pt.comma ? NumberFormat.getNumberInstance(Locale.US)
                            .format(r[c]) : r[c].toString();
                    nc = StringUtils.leftPad(n, maxWidth[c]);
                } else {
                    String colStr = String.valueOf(r[c]);
                    // 检查字符串是否可以解析为数字
                    boolean isNumeric = isNumericString(colStr);
                    int colorAnisSize = colorAnisSize(colStr);
                    // 修正中文补偿
                    int width = maxWidth[c] - fixLength(colStr);
                    int rLength = colStr.length();
                    
                    if (isNumeric) {
                        // 如果是数字字符串，使用左填充（右对齐）
                        if (colorAnisSize == 0) {
                            nc = StringUtils.leftPad(colStr, width);
                        } else {
                            int colL = colLength(colStr);
                            nc = StringUtils.leftPad(colStr, rLength + (width - colL) + colorAnisSize);
                        }
                    } else {
                        // 非数字字符串，使用右填充（左对齐）
                        if (colorAnisSize == 0) {
                            nc = StringUtils.rightPad(colStr, width);
                        } else {
                            int colL = colLength(colStr);
                            nc = StringUtils.rightPad(colStr, rLength + (width - colL) + colorAnisSize);
                        }
                    }
                }
                af(nc);
                if (c < rL - 1) {
                    centerBorder();
                } else {
                    rightBorder();
                }
            }
        }
        bottomBorderLine(maxWidth);
        return toString();
    }

    /**
     * Adjust for max width of the column
     *
     * @param pt
     * @return
     */
    public int[] adjustMaxWidth(PrettyTable pt) {
        // Adjust comma
        List<List<String>> converted = new ArrayList<>();
        for (Object[] r : pt.rows) {
            List<String> collect = Stream.of(r)
                    .map(o -> {
                        if (pt.comma && o instanceof Number) {
                            return NumberFormat.getNumberInstance(Locale.US)
                                    .format(o);
                        } else {
                            try {
                                return o.toString();
                            } catch (NullPointerException e) {
                                return "";
                            }
                        }
                    })
                    .collect(Collectors.toList());
            converted.add(collect);
        }
        return IntStream.range(0, pt.fieldNames.size())
                .map(i -> {
                    int n = converted.stream()
                            .map(f -> colLength(f.get(i)))
                            .max(Comparator.naturalOrder())
                            .orElse(0);
                    // 使用colLength计算表头字段名的长度，而不是直接使用length()
                    return Math.max(colLength(pt.fieldNames.get(i)), n);
                })
                .toArray();
    }

    private int colLength(String colStr) {
        // 去除 ANSI 颜色码（如 \u001B[0m），假设格式为 \u001B[xxm
        String ansiRegex = "\\u001B\\[[;\\d]*m";
        String cleanedStr = colStr.replaceAll(ansiRegex, "");
        int length = cleanedStr.length();
        // 补偿中文
        return length + fixLength(cleanedStr);
    }

    private int fixLength(String colStr) {
        int length1 = colStr.length();
        int length2 = getChineseLength(colStr);
        return length2 - length1;
    }

    /**
     * 判断字符串是否为数字（包括整数和浮点数）
     * 支持带千分位分隔符的数字和百分比
     *
     * @param str 要检查的字符串
     * @return 如果是数字返回true，否则返回false
     */
    private boolean isNumericString(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        
        String trimmed = str.trim();
        
        // 处理百分比
        if (trimmed.endsWith("%")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        
        // 移除千分位分隔符
        trimmed = trimmed.replace(",", "");
        
        try {
            Double.parseDouble(trimmed);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private int colorAnisSize(String str) {
        // 匹配 ANSI 颜色码（如 \u001B[0m）
        String ansiRegex = "\\u001B\\[[;\\d]*m";
        int originalLength = str.length();
        String cleanedStr = str.replaceAll(ansiRegex, "");
        int newLength = cleanedStr.length();
        return originalLength - newLength;
    }

    /**
     * 获取字符串的长度，如果有中文，则每个中文字符计为2位
     *
     * @param validateStr 指定的字符串
     * @return 字符串的长度
     */
    public static int getChineseLength(String validateStr) {
        int valueLength = 0;
        String chinese = "[Α-￥]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < validateStr.length(); i++) {
            /* 获取一个字符 */
            String temp = validateStr.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }

    private ConsoleConverter topBorderLine(final int[] maxWidth) {
        ab(border ? line(maxWidth) + "\n" : "");
        return this;
    }

    private ConsoleConverter bottomBorderLine(final int[] maxWidth) {
        ab(border ? "\n" + line(maxWidth) : "");
        return this;
    }

    private ConsoleConverter leftBorder() {
        ab(border ? "| " : "");
        return this;
    }

    private ConsoleConverter rightBorder() {
        ab(border ? " |" : "");
        return this;
    }

    private ConsoleConverter centerBorder() {
        ab(border ? " | " : " ");
        return this;
    }

    private static String line(final int[] maxWidth) {
        final StringBuilder sb = new StringBuilder();
        sb.append("+");
        int i = 0;
        while (i < maxWidth.length) {
            sb.append(StringUtils.rightPad("", maxWidth[i] + 2, '-'));
            sb.append("+");
            i++;
        }
        return sb.toString();
    }

}