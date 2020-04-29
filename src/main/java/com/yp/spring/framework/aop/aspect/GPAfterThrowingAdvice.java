package com.yp.spring.framework.aop.aspect;

import com.yp.spring.framework.aop.intercept.GPMethodInterceptor;
import com.yp.spring.framework.aop.intercept.GPMethodInvocation;

import java.lang.reflect.Method;
import java.security.PublicKey;

/**
 * @author ex-yipeng
 * @version Id: GPAfterThrowingAdvice.java, v 0.1 2020/4/29 10:26 ex-yipeng Exp $
 */
public class GPAfterThrowingAdvice extends GPAbstractAspectJAdvice implements GPAdvice, GPMethodInterceptor {

    private String throwingName;

    private GPMethodInvocation mi;

    public GPAfterThrowingAdvice(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    public void setThrowingName(String name) {
        this.throwingName = name;
    }

    @Override
    public Object invoke(GPMethodInvocation mi) throws Throwable {
        try {
            return mi.proceed();
        } catch (Exception e) {
            invokeAdviceMethod(mi, null, e.getCause());
            throw e;
        }
    }
}
