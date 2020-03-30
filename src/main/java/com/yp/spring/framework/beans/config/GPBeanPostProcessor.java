package com.yp.spring.framework.beans.config;

/**
 * @author ex-yipeng
 * @version Id: GPBeanPostProcessor.java, v 0.1 2020/3/30 15:49 ex-yipeng Exp $
 */
public class GPBeanPostProcessor {

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }
}
