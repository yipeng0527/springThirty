package com.yp.spring.demo.action;

import com.yp.spring.demo.service.IModifyService;
import com.yp.spring.demo.service.IQueryService;
import com.yp.spring.framework.annotation.GPAutowired;
import com.yp.spring.framework.annotation.GPController;
import com.yp.spring.framework.annotation.GPRequestMapping;
import com.yp.spring.framework.annotation.GPRequestParam;
import com.yp.spring.framework.webmvc.GPModelAndView;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 公布接口url
 *
 * @author Tom
 */
@GPController
@GPRequestMapping("/web")
public class MyAction {

    @GPAutowired
    private IQueryService queryService;

    @GPAutowired
    private IModifyService modifyService;

    @GPRequestMapping("/query.json")
    public GPModelAndView query(HttpServletRequest request, HttpServletResponse response,
                                @GPRequestParam("name") String name) {
        String result = queryService.query(name);
        return out(response, result);
    }

    @GPRequestMapping("/add*.json")
    public GPModelAndView add(HttpServletRequest request, HttpServletResponse response,
                              @GPRequestParam("name") String name, @GPRequestParam("addr") String addr) {
        String result = modifyService.add(name, addr);
        return out(response, result);
    }

    @GPRequestMapping("/remove.json")
    public GPModelAndView remove(HttpServletRequest request, HttpServletResponse response,
                                 @GPRequestParam("id") Integer id) {
        String result = modifyService.remove(id);
        return out(response, result);
    }

    @GPRequestMapping("/edit.json")
    public GPModelAndView edit(HttpServletRequest request, HttpServletResponse response,
                               @GPRequestParam("id") Integer id,
                               @GPRequestParam("name") String name) {
        String result = modifyService.edit(id, name);
        return out(response, result);
    }


    private GPModelAndView out(HttpServletResponse resp, String str) {
        try {
            resp.getWriter().write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
