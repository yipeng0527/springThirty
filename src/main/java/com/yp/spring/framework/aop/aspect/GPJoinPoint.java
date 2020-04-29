package com.yp.spring.framework.aop.aspect;

import java.lang.reflect.Method;

/**
 * @author ex-yipeng
 * @version Id: GPJoinPoint.java, v 0.1 2020/4/28 14:12 ex-yipeng Exp $
 */
public interface GPJoinPoint {

    /**
     * 业务方法本身
     *
     * @return
     */
    Method getMethod();

    /**
     * 该方法的实参列表
     *
     * @return
     */
    Object[] getArguments();

    /**
     * 该方法所属的实例对象
     *
     * @return
     */
    Object getThis();

    /**
     * 在JoinPoint中添加自定义属性
     *
     * @param key
     * @param value
     */
    void setUserAttribute(String key, Object value);

    /**
     * 从已添加的的自定义属性中获取一个属性值
     *
     * @param key
     * @return
     */
    Object getUserAttribute(String key);
}
