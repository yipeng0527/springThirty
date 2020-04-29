package com.yp.spring.framework.aop.intercept;

import com.yp.spring.framework.aop.aspect.GPJoinPoint;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 执行拦截器链 相当于Spring中ReflectiveMethodInvocation
 *
 * @author ex-yipeng
 * @version Id: GPMethodInvocation.java, v 0.1 2020/4/28 14:35 ex-yipeng Exp $
 */
public class GPMethodInvocation implements GPJoinPoint {

    private Object proxy;  //代理对象
    private Method method; //代理的目标方法
    private Object target; //代理的目标对象
    private Class<?> targetClass; //代理的目标类
    private Object[] arguments; //代理方法的实参列表
    private List<Object> interceptorsAndDynamicMethodMatchers;  //回调方法链

    private Map<String, Object> userAttributes;

    private int currentInterceptorIndex = -1;

    public GPMethodInvocation(Object proxy, Object target, Method method, Object[] arguments,
                              Class<?> targetClass, List<Object> interceptorsAndDynamicMethodMatchers) {
        this.proxy = proxy;
        this.target = target;
        this.targetClass = targetClass;
        this.method = method;
        this.arguments = arguments;
        this.interceptorsAndDynamicMethodMatchers = interceptorsAndDynamicMethodMatchers;
    }

    public Object proceed() throws Throwable {
        //如果Interceptor执行完了就执行joinPoint
        if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
            return method.invoke(this.target, this.arguments);
        }
        Object interceptorOrInterceptionAdvice =
                this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
        if (interceptorOrInterceptionAdvice instanceof GPMethodInterceptor) {
            GPMethodInterceptor mi = (GPMethodInterceptor) interceptorOrInterceptionAdvice;
            return mi.invoke(this);
        } else {
            //执行当前Interceptor
            return proceed();
        }
    }

    @Override
    public Method getMethod() {
        return this.method;
    }

    @Override
    public Object[] getArguments() {
        return this.arguments;
    }

    @Override
    public Object getThis() {
        return this.target;
    }

    @Override
    public void setUserAttribute(String key, Object value) {
        if (null != value) {
            if (null == this.userAttributes) {
                this.userAttributes = new HashMap<>();
            }
            this.userAttributes.put(key, value);
        } else {
            if (null != userAttributes) {
                userAttributes.remove(key);
            }
        }
    }

    @Override
    public Object getUserAttribute(String key) {
        return this.userAttributes != null ? this.userAttributes.get(key) : null;
    }
}
