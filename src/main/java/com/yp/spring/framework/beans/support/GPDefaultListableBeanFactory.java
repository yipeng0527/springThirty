package com.yp.spring.framework.beans.support;

import com.yp.spring.framework.beans.config.GPBeanDefinition;
import com.yp.spring.framework.context.support.GPAbstractApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ex-yipeng
 * @version Id: GPDefaultListableBeanFactory.java, v 0.1 2020/3/30 14:37 ex-yipeng Exp $
 */
public class GPDefaultListableBeanFactory extends GPAbstractApplicationContext {

    /**
     * 存贮注册信息的bean
     */
    protected final Map<String, GPBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
}
