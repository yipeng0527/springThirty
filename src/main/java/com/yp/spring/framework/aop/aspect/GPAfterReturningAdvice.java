package com.yp.spring.framework.aop.aspect;

import com.yp.spring.framework.aop.intercept.GPMethodInterceptor;
import com.yp.spring.framework.aop.intercept.GPMethodInvocation;

import java.lang.reflect.Method;

/**
 * @author ex-yipeng
 * @version Id: GPAfterReturningAdvice.java, v 0.1 2020/4/29 10:21 ex-yipeng Exp $
 */
public class GPAfterReturningAdvice extends GPAbstractAspectJAdvice implements GPAdvice, GPMethodInterceptor {

    private GPJoinPoint joinPoint;

    public GPAfterReturningAdvice(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(GPMethodInvocation mi) throws Throwable {
        Object retVal = mi.proceed();
        this.joinPoint = mi;
        this.afterReturning(retVal, mi.getMethod(), mi.getArguments(), mi.getThis());
        return retVal;
    }

    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        invokeAdviceMethod(joinPoint, returnValue, null);
    }


}
