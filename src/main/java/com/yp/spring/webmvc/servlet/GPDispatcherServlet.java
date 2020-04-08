package com.yp.spring.webmvc.servlet;

import com.yp.spring.framework.context.GPApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ex-yipeng
 * @version Id: GPDispatcherServlet.java, v 0.1 2020/3/30 13:55 ex-yipeng Exp $
 */
public class GPDispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GPApplicationContext applicationContext = new GPApplicationContext("application.properties");
        System.out.println("1111");
    }
}
