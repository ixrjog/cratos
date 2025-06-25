package com.baiyi.cratos.common.table.util;

import java.util.regex.Pattern;

/**
 * ANSI转义序列处理工具类
 */
public class AnsiUtils {
    
    // 更完整的ANSI转义序列正则表达式
    // 匹配所有ANSI转义序列：ESC[ + 参数 + 命令字符
    private static final Pattern ANSI_PATTERN = Pattern.compile(
        "\\u001B\\[[0-9;]*[a-zA-Z]|" +  // 标准ANSI序列 ESC[...m
        "\\u001B\\([AB]|" +              // 字符集选择
        "\\u001B\\][^\\u0007]*\\u0007|" + // OSC序列
        "\\u001B[=>]|" +                  // 应用程序键盘模式
        "\\u001B[78]"                    // 保存/恢复光标
    );
    
    // 中文字符正则表达式
    private static final Pattern CHINESE_PATTERN = Pattern.compile("[\u4e00-\u9fa5]");
    
    /**
     * 移除字符串中的所有ANSI转义序列
     * @param text 包含ANSI序列的文本
     * @return 清理后的纯文本
     */
    public static String stripAnsi(String text) {
        if (text == null) {
            return null;
        }
        return ANSI_PATTERN.matcher(text).replaceAll("");
    }
    
    /**
     * 计算字符串的显示宽度（考虑ANSI序列和中文字符）
     * @param text 文本
     * @return 显示宽度
     */
    public static int getDisplayWidth(String text) {
        if (text == null) {
            return 0;
        }
        
        // 先移除ANSI序列
        String cleanText = stripAnsi(text);
        
        // 计算显示宽度：中文字符占2个位置，其他字符占1个位置
        int width = 0;
        for (int i = 0; i < cleanText.length(); i++) {
            char c = cleanText.charAt(i);
            if (isChinese(c)) {
                width += 2;
            } else {
                width += 1;
            }
        }
        
        return width;
    }
    
    /**
     * 计算ANSI序列的字节长度
     * @param text 包含ANSI序列的文本
     * @return ANSI序列占用的字节数
     */
    public static int getAnsiLength(String text) {
        if (text == null) {
            return 0;
        }
        return text.length() - stripAnsi(text).length();
    }
    
    /**
     * 判断字符是否为中文字符
     * @param c 字符
     * @return 是否为中文字符
     */
    public static boolean isChinese(char c) {
        return c >= 0x4e00 && c <= 0x9fa5;
    }
    
    /**
     * 判断字符串是否包含中文字符
     * @param text 文本
     * @return 是否包含中文字符
     */
    public static boolean containsChinese(String text) {
        if (text == null) {
            return false;
        }
        return CHINESE_PATTERN.matcher(text).find();
    }
    
    /**
     * 计算中文字符的额外宽度补偿
     * @param text 文本
     * @return 中文字符的额外宽度
     */
    public static int getChineseWidthCompensation(String text) {
        if (text == null) {
            return 0;
        }
        
        String cleanText = stripAnsi(text);
        int chineseCount = 0;
        
        for (int i = 0; i < cleanText.length(); i++) {
            if (isChinese(cleanText.charAt(i))) {
                chineseCount++;
            }
        }
        
        return chineseCount; // 每个中文字符额外占用1个位置
    }
    
    /**
     * 安全的字符串填充，考虑ANSI序列和中文字符
     * @param text 原始文本
     * @param targetWidth 目标显示宽度
     * @param leftAlign 是否左对齐
     * @return 填充后的字符串
     */
    public static String padString(String text, int targetWidth, boolean leftAlign) {
        if (text == null) {
            text = "";
        }
        
        int displayWidth = getDisplayWidth(text);
        int ansiLength = getAnsiLength(text);
        
        if (displayWidth >= targetWidth) {
            return text; // 已经足够宽，不需要填充
        }
        
        int paddingNeeded = targetWidth - displayWidth;
        String padding = " ".repeat(paddingNeeded);
        
        if (leftAlign) {
            return text + padding;
        } else {
            return padding + text;
        }
    }
    
    /**
     * 创建测试用的带颜色文本
     * @param text 文本内容
     * @param colorCode ANSI颜色代码
     * @return 带颜色的文本
     */
    public static String colorText(String text, String colorCode) {
        return "\u001B[" + colorCode + "m" + text + "\u001B[0m";
    }
    
    // 常用颜色代码常量
    public static final String RED = "31";
    public static final String GREEN = "32";
    public static final String YELLOW = "33";
    public static final String BLUE = "34";
    public static final String MAGENTA = "35";
    public static final String CYAN = "36";
    public static final String WHITE = "37";
    public static final String RESET = "0";
}
