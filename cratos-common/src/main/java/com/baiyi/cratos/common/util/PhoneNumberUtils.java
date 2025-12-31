package com.baiyi.cratos.common.util;

/**
 * 国际手机号工具类
 *
 * @author baiyi
 */
public class PhoneNumberUtils {

    // 国际手机号正则（含国家码，支持分隔符）
    private static final String INTERNATIONAL_PHONE = "^\\+?[1-9][\\d\\-\\s]{6,14}$";
    // 中国手机号正则（不含国家码）
    private static final String CHINA_PHONE = "^1[3456789]\\d{9}$";

    /**
     * 国际手机号判断和转换
     *
     * @param phone 输入的手机号
     * @return 转换后的格式：国家码-手机号，无效返回"0"
     */
    public static String convertPhoneNumber(String phone) {
        if (phone == null || phone.trim()
                .isEmpty()) {
            return "0";
        }

        phone = phone.trim();
        if (phone.startsWith("+")) {
            phone = phone.substring(1);
        }

        // 检查是否为中国手机号（纯数字11位）
        if (phone.matches(CHINA_PHONE)) {
            return "86-" + phone;
        }

        // 如果已经是 国家码-手机号 格式，直接返回
        if (phone.matches("^\\d{1,3}-\\d{6,12}$")) {
            return phone;
        }

        // 检查是否为国际手机号格式
        if (phone.matches(INTERNATIONAL_PHONE)) {
            // 去掉+号和所有分隔符
            String cleanPhone = phone.replaceAll("[\\+\\-\\s]", "");

            // 根据长度和规则判断国家码位数
            if (cleanPhone.length() >= 7) {
                // 尝试1位国家码（美国、加拿大等）
                if (cleanPhone.startsWith("1") && cleanPhone.length() >= 8) {
                    return "1-" + cleanPhone.substring(1);
                }
                // 尝试2位国家码（大部分国家）
                if (cleanPhone.length() >= 8) {
                    String countryCode = cleanPhone.substring(0, 2);
                    String phoneNumber = cleanPhone.substring(2);
                    return countryCode + "-" + phoneNumber;
                }
                // 尝试3位国家码（少数国家）
                if (cleanPhone.length() >= 9) {
                    String countryCode = cleanPhone.substring(0, 3);
                    String phoneNumber = cleanPhone.substring(3);
                    return countryCode + "-" + phoneNumber;
                }
            }
        }

        return "0";
    }

    /**
     * 验证是否为有效手机号
     *
     * @param phone 手机号
     * @return true-有效，false-无效
     */
    public static boolean isValidPhoneNumber(String phone) {
        return !"0".equals(phone);
    }

}
