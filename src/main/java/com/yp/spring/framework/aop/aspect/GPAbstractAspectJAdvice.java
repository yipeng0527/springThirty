package com.yp.spring.framework.aop.aspect;

import java.lang.reflect.Method;

/**
 * @author ex-yipeng
 * @version Id: GPAbstractAspectJAdvice.java, v 0.1 2020/4/28 17:12 ex-yipeng Exp $
 */
public abstract class GPAbstractAspectJAdvice implements GPAdvice {

    private Method aspectMethod;

    private Object aspectTarget;

    public GPAbstractAspectJAdvice(Method aspectMethod, Object aspectTarget) {
        this.aspectMethod = aspectMethod;
        this.aspectTarget = aspectTarget;
    }

    protected Object invokeAdviceMethod(GPJoinPoint joinPoint, Object returnValue, Throwable ex) throws Throwable {
        Class<?>[] paramsTypes = this.aspectMethod.getParameterTypes();
        if (null == paramsTypes || paramsTypes.length == 0) {
            return this.aspectMethod.invoke(aspectTarget);
        } else {
            Object[] args = new Object[paramsTypes.length];
            for (int i = 0; i < paramsTypes.length; i++) {
                if (paramsTypes[i] == GPJoinPoint.class) {
                    args[i] = joinPoint;
                } else if (paramsTypes[i] == Throwable.class) {
                    args[i] = ex;
                } else if (paramsTypes[i] == Object.class) {
                    args[i] = returnValue;
                }
            }
            return this.aspectMethod.invoke(aspectTarget, args);
        }
    }
}
