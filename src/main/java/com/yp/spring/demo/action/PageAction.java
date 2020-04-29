package com.yp.spring.demo.action;

import com.google.common.collect.Maps;
import com.yp.spring.demo.service.IQueryService;
import com.yp.spring.framework.annotation.GPAutowired;
import com.yp.spring.framework.annotation.GPController;
import com.yp.spring.framework.annotation.GPRequestMapping;
import com.yp.spring.framework.annotation.GPRequestParam;
import com.yp.spring.framework.webmvc.GPModelAndView;

import java.util.Map;

/**
 * @author ex-yipeng
 * @version Id: PageAction.java, v 0.1 2020/4/10 16:21 ex-yipeng Exp $
 */
@GPController
@GPRequestMapping("/")
public class PageAction {

    @GPAutowired
    private IQueryService queryService;

    @GPRequestMapping("/layouts/first.html")
    public GPModelAndView query(@GPRequestParam("teacher") String teacher) {
        String result = queryService.query(teacher);
        Map<String, Object> model = Maps.newHashMap();
        model.put("teacher", teacher);
        model.put("data", result);
        model.put("token", "123456");
        return new GPModelAndView("first.html", model);
    }

}
