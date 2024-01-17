package com.baiyi.cratos.facade;

/**
 * @Author baiyi
 * @Date 2024/1/17 10:05
 * @Version 1.0
 */
public interface RbacFacade {

    /**
     * 验证资源访问权限
     * @param token
     * @param resource
     */
   void verifyResourceAccessPermissions(String token, String resource);

}
