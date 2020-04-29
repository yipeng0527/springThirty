package com.yp.spring.framework.webmvc;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

import com.google.gson.Gson;

/**
 * @author ex-yipeng
 * @version Id: GPHandlerMapping.java, v 0.1 2020/4/10 13:22 ex-yipeng Exp $
 */
public class GPHandlerMapping {

    private Object controller;  //保存方法对应的实例

    private Method method; //保存映射的方法

    private Pattern pattern;  //URL的正则匹配

    public GPHandlerMapping(Object controller, Method method, Pattern pattern) {
        this.controller = controller;
        this.method = method;
        this.pattern = pattern;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }
}
