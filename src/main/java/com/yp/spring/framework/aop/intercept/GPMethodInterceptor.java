package com.yp.spring.framework.aop.intercept;

/**
 * 方法拦截器顶层接口
 *
 * @author ex-yipeng
 * @version Id: GPMethodInterceptor.java, v 0.1 2020/4/28 14:19 ex-yipeng Exp $
 */
public interface GPMethodInterceptor {
    Object invoke(GPMethodInvocation mi) throws Throwable;
}
