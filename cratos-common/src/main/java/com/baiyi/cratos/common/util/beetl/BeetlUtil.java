package com.baiyi.cratos.common.util.beetl;

import lombok.NoArgsConstructor;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.exception.BeetlException;
import org.beetl.core.resource.StringTemplateResourceLoader;

import java.io.IOException;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/7 下午4:42
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public final class BeetlUtil {

    /**
     * 渲染模板
     *
     * @param template
     * @param contentMap
     * @return
     * @throws IOException
     */
    public static String renderTemplate(String template,
                                        Map<String, Object> contentMap) throws IOException, BeetlException {
        // 初始化代码
        StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
        Configuration cfg = Configuration.defaultConfiguration();
        GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
        // 获取模板
        Template t = gt.getTemplate(template);
        t.binding(contentMap);
        return t.render();
    }

    public static String renderTemplateV2(String template,
                                          Map<String, String> contentMap) throws IOException, BeetlException {
        // 初始化代码
        StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
        Configuration cfg = Configuration.defaultConfiguration();
        GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
        // 获取模板
        Template t = gt.getTemplate(template);
        gt.setErrorHandler(new BeetlErrorHandler());
        t.binding(contentMap);
        return t.render();
    }

}
