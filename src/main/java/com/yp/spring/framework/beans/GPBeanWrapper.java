package com.yp.spring.framework.beans;

/**
 * @author ex-yipeng
 * @version Id: GPBeanWrapper.java, v 0.1 2020/3/30 14:29 ex-yipeng Exp $
 */
public class GPBeanWrapper {

    private Object wrappedInstance;

    private Class<?> wrappedClass;

    public GPBeanWrapper(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
    }

    public Object getWrappedInstance() {
        return this.wrappedInstance;
    }

    public Class<?> getWrappedClass() {
        return this.wrappedInstance.getClass();
    }
}
