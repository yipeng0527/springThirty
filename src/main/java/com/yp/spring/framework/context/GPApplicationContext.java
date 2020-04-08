package com.yp.spring.framework.context;

import com.yp.spring.framework.annotation.GPAutowired;
import com.yp.spring.framework.annotation.GPController;
import com.yp.spring.framework.annotation.GPService;
import com.yp.spring.framework.beans.GPBeanWrapper;
import com.yp.spring.framework.beans.config.GPBeanDefinition;
import com.yp.spring.framework.beans.config.GPBeanPostProcessor;
import com.yp.spring.framework.beans.support.GPBeanDefinitionReader;
import com.yp.spring.framework.beans.support.GPDefaultListableBeanFactory;
import com.yp.spring.framework.core.GPBeanFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ex-yipeng
 * @version Id: GPApplicationContext.java, v 0.1 2020/3/30 14:41 ex-yipeng Exp $
 */
public class GPApplicationContext extends GPDefaultListableBeanFactory implements GPBeanFactory {

    /**
     * 读取文件的数组
     */
    private String[] configLocations;

    /**
     * beanDefinitionReader
     */
    private GPBeanDefinitionReader reader;

    /**
     * 用来保存注册式单例的容器
     */
    private Map<String, Object> singletonBeanCacheMap = new ConcurrentHashMap<>();

    /**
     * 用来存储所有被代理过的对象
     */
    private Map<String, GPBeanWrapper> beanWrapperMap = new ConcurrentHashMap<>();

    public GPApplicationContext(String... configLocations) {
        this.configLocations = configLocations;
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refresh() throws Exception {
        //定位配置文件
        reader = new GPBeanDefinitionReader(this.configLocations);
        //加载配置文件
        List<GPBeanDefinition> beanDefinitions = reader.loadBeanDefinitions();
        //注册 把配置信息放在容器里面(伪IOC容器)
        doRegisterBeanDefinition(beanDefinitions);
        //把不是延迟加载的类 提前初始化
        doAutowrited();
    }

    private void doAutowrited() {
        for (Map.Entry<String, GPBeanDefinition> beanDefinitionEntry : super.beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            if (!beanDefinitionEntry.getValue().getLazyInit()) {
                try {
                    getBean(beanName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void doRegisterBeanDefinition(List<GPBeanDefinition> beanDefinitions) throws Exception {
        for (GPBeanDefinition beanDefinition : beanDefinitions) {
            if (super.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
                throw new Exception("The " + beanDefinition.getFactoryBeanName() + " is exist !");
            }
            super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
        }
    }

    @Override
    public Object getBean(String beanName) throws Exception {
        GPBeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
        try {
            //生成事件通知
            GPBeanPostProcessor beanPostProcessor = new GPBeanPostProcessor();
            Object instance = instantiateBean(beanDefinition);
            if (null == instance) {
                return null;
            }
            //在实例化前调用一次
            beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
            GPBeanWrapper beanWrapper = new GPBeanWrapper(instance);
            this.beanWrapperMap.put(beanName, beanWrapper);
            //在实例化后调用一次
            beanPostProcessor.postProcessAfterInitialization(instance, beanName);
            populateBean(beanName, instance);
            return this.beanWrapperMap.get(beanName).getWrappedInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void populateBean(String beanName, Object instance) {
        Class clazz = instance.getClass();
        if (!(clazz.isAnnotationPresent(GPController.class) || clazz.isAnnotationPresent(GPService.class))) {
            return;
        }

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(GPAutowired.class)) {
                continue;
            }
            GPAutowired autowired = field.getAnnotation(GPAutowired.class);
            String autowiredBeanName = autowired.value().trim();
            if ("".equals(autowiredBeanName)) {
                autowiredBeanName = field.getType().getName();
            }
            //强制访问
            field.setAccessible(true);
            try {
                field.set(instance, this.beanWrapperMap.get(autowiredBeanName).getWrappedInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Object instantiateBean(GPBeanDefinition beanDefinition) {
        Object instance = null;
        String className = beanDefinition.getBeanClassName();
        try {
            if (this.singletonBeanCacheMap.containsKey(className)) {
                instance = this.singletonBeanCacheMap.get(className);
            } else {
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();
                this.singletonBeanCacheMap.put(beanDefinition.getFactoryBeanName(), instance);
            }
            return instance;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Object getBean(Class<?> beanClass) throws Exception {
        return getBean(beanClass.getName());
    }

    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
    }

    public int getBeanDefinitionCount() {
        return beanDefinitionMap.size();
    }

    public Properties getConfig() {
        return reader.getConfig();
    }
}
