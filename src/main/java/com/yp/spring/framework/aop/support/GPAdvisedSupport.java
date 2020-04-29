package com.yp.spring.framework.aop.support;

import com.yp.spring.framework.aop.GPAopConfig;
import com.yp.spring.framework.aop.aspect.GPAfterReturningAdvice;
import com.yp.spring.framework.aop.aspect.GPAfterThrowingAdvice;
import com.yp.spring.framework.aop.aspect.GPMethodBeforeAdvice;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 主要用来解析和封装AOP的配置
 *
 * @author ex-yipeng
 * @version Id: GPAdvisedSupport.java, v 0.1 2020/4/28 14:34 ex-yipeng Exp $
 */
public class GPAdvisedSupport {

    private Class targetClass;

    private Object target;

    private Pattern pointCutClassPattern;

    private transient Map<Method, List<Object>> methodCache;

    private GPAopConfig config;

    public GPAdvisedSupport(GPAopConfig config) {
        this.config = config;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
        parse();
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method,
                                                                    Class<?> targetClass) throws Exception {
        List<Object> cached = methodCache.get(method);
        //缓存未命中 则进行下一步处理
        if (null == cached) {
            Method m = targetClass.getMethod(method.getName(), method.getParameterTypes());
            cached = methodCache.get(m);
            //存入缓存
            this.methodCache.put(method, cached);
        }
        return cached;
    }

    public boolean pointCutMatch() {
        return pointCutClassPattern.matcher(this.targetClass.toString()).matches();
    }

    private void parse() {
        //pointCut表达式
        String pointCut = config.getPointCut()
                .replaceAll("\\.", "\\\\.")
                .replaceAll("\\\\.\\*", ".*")
                .replaceAll("\\(", "\\\\(")
                .replaceAll("\\)", "\\\\)");
        String pointCutForClass = pointCut.substring(0, pointCut.lastIndexOf("\\(") - 4);
        pointCutClassPattern = Pattern.compile("class" + pointCutForClass
                .substring(pointCutForClass.lastIndexOf(" ") + 1));
        methodCache = new HashMap<>();
        Pattern pattern = Pattern.compile(pointCut);
        try {
            Class aspectClass = Class.forName(config.getAspectClass());
            Map<String, Method> aspectMethods = new HashMap<>();
            for (Method m : aspectClass.getMethods()) {
                aspectMethods.put(m.getName(), m);
            }
            //在这里得到的方法都是原生方法
            for (Method m : targetClass.getMethods()) {
                String methodString = m.toString();
                if (methodString.contains("throws")) {
                    methodString = methodString.substring(0, methodString.lastIndexOf("throws")).trim();
                }
                Matcher matcher = pattern.matcher(methodString);
                if (matcher.matches()) {
                    //能满足切面规则的类,添加到AOP配置中
                    List<Object> advices = new LinkedList<>();
                    //前置通知
                    if (!(null == config.getAspectBefore() || "".equals(config.getAspectBefore().trim()))) {
                        advices.add(new GPMethodBeforeAdvice(aspectMethods.get(config.getAspectBefore()), aspectClass.newInstance()));
                    } else if (!(null == config.getAspectAfter() || "".equals(config.getAspectAfter().trim()))) {
                        advices.add(new GPAfterReturningAdvice(aspectMethods.get(config.getAspectAfter()), aspectClass.newInstance()));
                    } else if (!(null == config.getAspectAfterThrow() || "".equals(config.getAspectAfterThrow().trim()))) {
                        GPAfterThrowingAdvice afterThrowingAdvice = new GPAfterThrowingAdvice(
                                aspectMethods.get(config.getAspectAfterThrow()), aspectClass.newInstance()
                        );
                        afterThrowingAdvice.setThrowingName(config.getAspectAfterThrowingName());
                        advices.add(afterThrowingAdvice);
                    }
                    methodCache.put(m,advices);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
