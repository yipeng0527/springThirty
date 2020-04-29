package com.yp.spring.demo.aspect;

import com.yp.spring.framework.aop.aspect.GPJoinPoint;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @author ex-yipeng
 * @version Id: LogAspect.java, v 0.1 2020/4/29 10:58 ex-yipeng Exp $
 */
@Slf4j
public class LogAspect {

    /**
     * 在调用一个方法之前,执行before方法
     */
    public void before(GPJoinPoint joinPoint) {
        log.info("Invoker Before Method !!!" +
                "\nTargetObject:" + joinPoint.getThis() +
                "\nArgs:" + Arrays.toString(joinPoint.getArguments()));
    }

    /**
     * 在调用一个方法之后,执行after方法
     *
     * @param joinPoint
     */
    public void after(GPJoinPoint joinPoint) {
        log.info("Invoker After Method !!!" +
                "\nTargetObject:" + joinPoint.getThis() +
                "\nArgs:" + Arrays.toString(joinPoint.getArguments()));
    }

    /**
     * 在出现异常之后,执行afterThrowing方法
     *
     * @param joinPoint
     * @param ex
     */
    public void afterThrowing(GPJoinPoint joinPoint, Throwable ex) {
        log.info("出现异常 !!!" +
                "\nTargetObject:" + joinPoint.getThis() +
                "\nArgs:" + Arrays.toString(joinPoint.getArguments())
                + "\nThorw:" + ex.getMessage());
    }
}
