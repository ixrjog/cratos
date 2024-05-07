package com.baiyi.cratos.common.util;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;

import java.io.IOException;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/7 下午4:42
 * &#064;Version 1.0
 */
public class BeetlUtil {

    private BeetlUtil() {
    }

    /**
     * 渲染模板
     *
     * @param template
     * @param contentMap
     * @return
     * @throws IOException
     */
    public static String renderTemplate(String template, Map<String, Object> contentMap) throws IOException {
        // 初始化代码
        StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
        Configuration cfg = Configuration.defaultConfiguration();
        GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
        // 获取模板
        Template t = gt.getTemplate(template);
        t.binding(contentMap);
        return t.render();
    }

}
