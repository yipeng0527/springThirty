package com.yp.spring.framework.aop;

/**
 * 代理工厂的顶层接口，提供获取代理对象的顶层入口
 * 默认就用jdk的动态代理
 *
 * @author ex-yipeng
 * @version Id: GPAopProxy.java, v 0.1 2020/4/28 15:12 ex-yipeng Exp $
 */
public interface GPAopProxy {

    /**
     * 获得一个代理对象
     *
     * @return
     */
    Object getProxy();

    /**
     * 通过自定义类加载器获得一个代理对象
     *
     * @param classLoader
     * @return
     */
    Object getProxy(ClassLoader classLoader);
}
