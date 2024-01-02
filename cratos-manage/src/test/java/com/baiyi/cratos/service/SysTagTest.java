package com.baiyi.cratos.service;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.generator.SysTag;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/2 13:39
 * @Version 1.0
 */
public class SysTagTest extends BaseUnit {

    @Resource
    private SysTagService tagService;

    @Test
    void test() {
        List<SysTag> tags = tagService.selectAll();
        tags.forEach(t -> System.out.println(t.getTagKey()));
    }

}