package com.yp.spring.framework.aop;

import com.yp.spring.framework.aop.intercept.GPMethodInvocation;
import com.yp.spring.framework.aop.support.GPAdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @author ex-yipeng
 * @version Id: GPJdkDynamicAopProxy.java, v 0.1 2020/4/28 15:19 ex-yipeng Exp $
 */
public class GPJdkDynamicAopProxy implements GPAopProxy, InvocationHandler {

    private GPAdvisedSupport config;

    public GPJdkDynamicAopProxy(GPAdvisedSupport config) {
        this.config = config;
    }

    @Override
    public Object getProxy() {
        return getProxy(this.config.getTargetClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader, this.config.getTargetClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<Object> interceptorsAndDynamicMehodMatchers = config.getInterceptorsAndDynamicInterceptionAdvice(
                method, this.config.getTargetClass());
        GPMethodInvocation invocation = new GPMethodInvocation(proxy, this.config.getTarget(),
                method, args, this.config.getTargetClass(), interceptorsAndDynamicMehodMatchers);
        return invocation.proceed();
    }
}
