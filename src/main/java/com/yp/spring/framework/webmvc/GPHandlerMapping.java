package com.yp.spring.framework.webmvc;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

import com.google.gson.Gson;

/**
 * @author ex-yipeng
 * @version Id: GPHandlerMapping.java, v 0.1 2020/4/10 13:22 ex-yipeng Exp $
 */
public class GPHandlerMapping {

    private Object controller;

    private Method method;

    private Pattern pattern;

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

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
