package com.baiyi.cratos.sensitive;

import com.baiyi.cratos.annotation.Sensitive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/8 14:21
 * @Version 1.0
 */
@Slf4j
@Component
public class SensitiveVOTest {

    @Sensitive
    public void wrap(SensitiveTest.SensitiveVO vo) {
        log.info("{}", vo);
    }

}
