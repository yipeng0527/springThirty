package com.yp.spring.framework.aop.aspect;

import com.yp.spring.framework.aop.intercept.GPMethodInterceptor;
import com.yp.spring.framework.aop.intercept.GPMethodInvocation;

import java.lang.reflect.Method;

/**
 * @author ex-yipeng
 * @version Id: GPMethodBeforeAdvice.java, v 0.1 2020/4/29 10:12 ex-yipeng Exp $
 */
public class GPMethodBeforeAdvice extends GPAbstractAspectJAdvice implements GPAdvice, GPMethodInterceptor {

    private GPJoinPoint joinPoint;

    public GPMethodBeforeAdvice(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    public void before(Method method, Object[] args, Object target) throws Throwable {
        invokeAdviceMethod(this.joinPoint, null, null);
    }

    @Override
    public Object invoke(GPMethodInvocation mi) throws Throwable {
        this.joinPoint = mi;
        this.before(mi.getMethod(), mi.getArguments(), mi.getThis());
        return mi.proceed();
    }
}
