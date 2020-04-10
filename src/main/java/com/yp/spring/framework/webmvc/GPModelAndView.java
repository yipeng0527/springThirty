package com.yp.spring.framework.webmvc;

import java.util.Map;
import com.google.gson.Gson;

/**
 * @author ex-yipeng
 * @version Id: GPModelAndView.java, v 0.1 2020/4/10 13:46 ex-yipeng Exp $
 */
public class GPModelAndView {

    private String viewName;

    private Map<String, ?> model;

    public GPModelAndView(String viewName) {
        this(viewName, null);
    }

    public GPModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<String, ?> getModel() {
        return model;
    }

    public void setModel(Map<String, ?> model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
