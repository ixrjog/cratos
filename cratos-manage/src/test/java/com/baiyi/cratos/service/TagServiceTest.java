package com.baiyi.cratos.service;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.generator.Tag;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/2 13:39
 * @Version 1.0
 */
public class TagServiceTest extends BaseUnit {

    @Resource
    private TagService tagService;

    @Test
    void test() {
        List<Tag> tags = tagService.selectAll();
        tags.forEach(t -> System.out.println(t.getTagKey()));
    }

}