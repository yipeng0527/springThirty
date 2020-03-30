package com.yp.spring.framework.core;

/**
 * 单例工厂的顶层设计
 *
 * @author ex-yipeng
 * @version Id: GPBeanFactory.java, v 0.1 2020/3/30 14:20 ex-yipeng Exp $
 */
public interface GPBeanFactory {

    Object getBean(String beanName) throws Exception;

    Object getBean(Class<?> beanClass) throws Exception;
}
