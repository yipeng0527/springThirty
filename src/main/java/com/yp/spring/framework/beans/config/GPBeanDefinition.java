package com.yp.spring.framework.beans.config;

/**
 * @author ex-yipeng
 * @version Id: GPBeanDefinition.java, v 0.1 2020/3/30 14:26 ex-yipeng Exp $
 */
public class GPBeanDefinition {

    private String beanClassName;

    private Boolean lazyInit;

    private String factoryBeanName;

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public Boolean getLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(Boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }
}
