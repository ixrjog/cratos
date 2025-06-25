package com.baiyi.cratos.common.table.converter;


import com.baiyi.cratos.common.table.Bordered;
import com.baiyi.cratos.common.table.Converter;
import com.baiyi.cratos.common.table.PrettyTable;
import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
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
        if (pt.getFieldNames().isEmpty()) {
            return "";
        }
        int[] maxWidth = adjustMaxWidth(pt);
        topBorderLine(maxWidth);
        leftBorder();
        // 渲染表头
        List<String> fieldNames = pt.getFieldNames();
        for (int i = 0; i < fieldNames.size(); i++) {
            String headerText = fieldNames.get(i);
            int chineseCompensation = fixLength(headerText);
            af(StringUtils.rightPad(headerText, maxWidth[i] - chineseCompensation));
            if (i < fieldNames.size() - 1) {
                centerBorder();
            } else {
                rightBorder();
            }
        }
        bottomBorderLine(maxWidth);
        // 渲染数据行
        for (Object[] r : pt.getRows()) {
            ab("\n");
            leftBorder();
            for (int c = 0; c < r.length && c < fieldNames.size(); c++) { // 添加边界检查
                String nc;
                Object cell = r[c];
                if (cell instanceof Number) {
                    String n = pt.isComma() ? NumberFormat.getNumberInstance(Locale.US)
                            .format(cell) : cell.toString();
                    nc = StringUtils.leftPad(n, maxWidth[c]);
                } else {
                    String colStr = String.valueOf(cell);
                    boolean isNumeric = isNumericString(colStr);
                    int ansiSize = colorAnisSize(colStr);
                    
                    // 计算实际显示宽度（去除ANSI码和中文补偿）
                    int displayWidth = colLength(colStr);
                    int targetWidth = maxWidth[c];
                    int paddingNeeded = targetWidth - displayWidth;
                    
                    // 计算最终字符串长度（原始长度 + 填充长度）
                    int finalLength = colStr.length() + paddingNeeded;
                    
                    if (isNumeric) {
                        nc = StringUtils.leftPad(colStr, finalLength);
                    } else {
                        nc = StringUtils.rightPad(colStr, finalLength);
                    }
                }
                af(nc);
                if (c < r.length - 1 && c < fieldNames.size() - 1) { // 添加边界检查
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
     * @param pt PrettyTable instance
     * @return array of max widths for each column
     */
    public int[] adjustMaxWidth(PrettyTable pt) {
        // 预处理所有行，转换为字符串并格式化千分位
        List<List<String>> converted = pt.getRows().stream()
                .map(r -> Stream.of(r)
                        .map(o -> {
                            if (pt.isComma() && o instanceof Number) {
                                return NumberFormat.getNumberInstance(Locale.US)
                                        .format(o);
                            }
                            return o == null ? "" : o.toString();
                        })
                        .collect(Collectors.toList()))
                .toList();
        
        int fieldCount = pt.getFieldNames().size();
        int[] maxWidths = new int[fieldCount];

        for (int i = 0; i < fieldCount; i++) {
            int headerWidth = colLength(pt.getFieldNames().get(i));
            int finalI = i;
            int maxDataWidth = converted.stream()
                    .filter(row -> row.size() > finalI) // 添加边界检查
                    .mapToInt(row -> colLength(row.get(finalI)))
                    .max()
                    .orElse(0);
            maxWidths[i] = Math.max(headerWidth, maxDataWidth);
        }
        return maxWidths;
    }

    private int colLength(String colStr) {
        if (colStr == null) {
            return 0;
        }
        // 去除 ANSI 颜色码，支持更完整的ANSI转义序列
        String ansiRegex = "\\u001B\\[[0-9;]*[a-zA-Z]";
        String cleanedStr = colStr.replaceAll(ansiRegex, "");
        int length = cleanedStr.length();
        // 补偿中文字符
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
        if (str == null || str.trim()
                .isEmpty()) {
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
        if (str == null) {
            return 0;
        }
        // 匹配更完整的 ANSI 转义序列
        // 包括颜色码、光标控制、清屏等
        String ansiRegex = "\\u001B\\[[0-9;]*[a-zA-Z]";
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
        if (validateStr == null || validateStr.isEmpty()) {
            return 0;
        }
        
        int valueLength = 0;
        // 修复：使用正确的中文Unicode范围
        String chinese = "[\u4e00-\u9fa5]";
        
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