package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.facade.application.ApplicationResourceBaselineFacade;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/30 16:05
 * &#064;Version 1.0
 */
public class ApplicationResourceBaselineFacadeTest extends BaseUnit {

    @Resource
    private ApplicationResourceBaselineFacade applicationResourceBaselineFacade;

    @Test
    void scanAllTest() {
        applicationResourceBaselineFacade.scanAll();
    }

}
