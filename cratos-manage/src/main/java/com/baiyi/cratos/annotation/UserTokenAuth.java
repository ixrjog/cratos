package com.baiyi.cratos.annotation;

import java.lang.annotation.*;

/**
 * 用户Token认证注解
 * 用于工单审批系统中验证审批者身份和权限的注解
 * 
 * 功能说明：
 * 1. 验证工单订阅者token的有效性
 * 2. 验证审批者用户的有效性和过期时间
 * 3. 检查工单状态是否为待审批状态
 * 4. 确保工单与订阅者的关联性
 * 5. 实现一次性token机制，防止重复使用
 * 6. 自动将审批者用户名写入session
 * 
 * 使用示例：
 * <pre>
 * {@code
 * @UserTokenAuth(
 *     ofTicketNo = "#ticketNo", 
 *     ofUsername = "#username", 
 *     ofToken = "#token"
 * )
 * @PostMapping("/approve")
 * public void approveTicket(String ticketNo, String username, String token) {
 *     // 审批逻辑
 * }
 * }
 * </pre>
 * 
 * &#064;Author  baiyi
 * &#064;Date  2025/7/10 13:44
 * &#064;Version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface UserTokenAuth {

    /**
     * 工单号的SpEL表达式
     * 用于从方法参数中获取工单号，支持SpEL表达式解析
     * 
     * @return 工单号的SpEL表达式，如 "#ticketNo"
     */
    String ofTicketNo();

    /**
     * 用户名的SpEL表达式
     * 用于从方法参数中获取审批者用户名，支持SpEL表达式解析
     * 
     * @return 用户名的SpEL表达式，如 "#username"
     */
    String ofUsername();

    /**
     * 认证Token的SpEL表达式
     * 用于从方法参数中获取一次性认证token，支持SpEL表达式解析
     * 注意：token使用后会立即失效，确保安全性
     * 
     * @return token的SpEL表达式，如 "#token"
     */
    String ofToken();

}
