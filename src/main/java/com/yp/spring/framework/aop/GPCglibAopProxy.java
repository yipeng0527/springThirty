package com.yp.spring.framework.aop;

import com.yp.spring.framework.aop.support.GPAdvisedSupport;

/**
 * 通过cglib生成代理类
 *
 * @author ex-yipeng
 * @version Id: GPCglibAopProxy.java, v 0.1 2020/4/28 15:16 ex-yipeng Exp $
 */
public class GPCglibAopProxy implements GPAopProxy {

    private GPAdvisedSupport config;

    public GPCglibAopProxy(GPAdvisedSupport config) {
        this.config = config;
    }

    @Override
    public Object getProxy() {
        return null;
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return null;
    }
}
