package com.baiyi.cratos.sensitive;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.annotation.FieldSensitive;
import com.baiyi.cratos.domain.enums.SensitiveType;
import jakarta.annotation.Resource;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2024/1/8 14:10
 * @Version 1.0
 */
@Slf4j
public class SensitiveTest extends BaseUnit {

    @Resource
    private SensitiveVOTest sensitiveVOTest;

    @Data
    @Builder
    @FieldSensitive
    public static class SensitiveVO {

        @FieldSensitive(type = SensitiveType.CUSTOMER, prefixNoMaskLen = 1, suffixNoMaskLen = 1)
        @Builder.Default
        private String name = "杭州小老徐";

        @FieldSensitive(type = SensitiveType.PASSWORD)
        @Builder.Default
        private String password = "123456";

        @FieldSensitive(type = SensitiveType.MOBILE_PHONE)
        @Builder.Default
        private String phone1 = "12356789999";

        @FieldSensitive(type = SensitiveType.MOBILE_PHONE)
        @Builder.Default
        private String phone2 = "021213131";

        @FieldSensitive(type = SensitiveType.MOBILE_PHONE)
        @Builder.Default
        private String phone3 = "10080";

        @FieldSensitive(type = SensitiveType.EMAIL, symbol = "&")
        @Builder.Default
        private String mail = "xiaobai@gmail.com";

        @FieldSensitive(type = SensitiveType.EMAIL)
        @Builder.Default
        private String mail2 = "my@gmail.com";

        @FieldSensitive(type = SensitiveType.EMAIL)
        @Builder.Default
        private String mail3 = "ddd";

    }

    @Test
    void sensitiveTest() {
        SensitiveVO vo = SensitiveVO.builder().build();
        sensitiveVOTest.wrap(vo);
        log.info("vo: {}", vo);
    }

}
