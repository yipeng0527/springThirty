package com.yp.spring.framework.aop;

import lombok.Data;

/**
 * AOP配置封装
 * @author ex-yipeng
 * @version Id: GPAopConfig.java, v 0.1 2020/4/28 14:23 ex-yipeng Exp $
 */
@Data
public class GPAopConfig {

    /**
     * 切面表达式
     */
    private String pointCut;

    /**
     * 前置通知方法名
     */
    private String aspectBefore;

    /**
     * 后置通知方法名
     */
    private String aspectAfter;

    /**
     * 要织入的切面类
     */
    private String aspectClass;

    /**
     * 异常通知方法名
     */
    private String aspectAfterThrow;

    /**
     * 需要通知的异常类型
     */
    private String aspectAfterThrowingName;
}
