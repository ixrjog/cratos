package com.baiyi.cratos;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.retry.annotation.Retryable;
import org.springframework.test.context.ActiveProfiles;

/**
 * @Author baiyi
 * @Date 2024/1/2 13:38
 * @Version 1.0
 */
@SpringBootTest(classes = ManageApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"ssh.shell.port=3001"})
@AutoConfigureMockMvc
// 单元测试读取dev配置文件
@ActiveProfiles(profiles = "dev")
@Retryable
public class BaseUnit {

}