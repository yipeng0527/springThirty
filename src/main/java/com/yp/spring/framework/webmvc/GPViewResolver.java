package com.yp.spring.framework.webmvc;

import java.io.File;
import java.util.Locale;

/**
 * @author ex-yipeng
 * @version Id: GPViewResolver.java, v 0.1 2020/4/10 13:56 ex-yipeng Exp $
 */
//一个静态文件变为一个动态文件 根据用户传参的不同 产生不同的结果 最终输出字符串交给Response输出
public class GPViewResolver {

    private final String DEFAULT_TEMPLATE_SUFFIX = ".html";

    private File templateRootDir;

    private String viewName;

    public GPViewResolver(String templateRootDir) {
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRootDir).getFile();
        this.templateRootDir = new File(templateRootPath);
    }

    public GPView resolveViewName(String viewName, Locale locale) {
        this.viewName = viewName;
        if (null == viewName || "".equals(viewName.trim())) {
            return null;
        }
        viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFIX) ? viewName : (viewName + DEFAULT_TEMPLATE_SUFFIX);
        File templateFile = new File((templateRootDir.getPath() + "/" + viewName).replaceAll("/+", "/"));
        return new GPView(templateFile);
    }

    public String getViewName() {
        return viewName;
    }
}
