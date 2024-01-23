package com.baiyi.cratos.redis;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.common.RedisUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2024/1/23 13:14
 * @Version 1.0
 */
public class RedisTest extends BaseUnit {

    @Resource
    private RedisUtil redisUtil;


    private static final String TEST_KEY = "OC5:REDIS:TEST:v1";

    @Test
    void test(){
        redisUtil.set(TEST_KEY,"12345");

        System.out.println(redisUtil.get(TEST_KEY));

    }

}
