package com.yp.spring.framework.webmvc;

import com.google.common.collect.Maps;
import com.yp.spring.framework.annotation.GPRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;

/**
 * @author ex-yipeng
 * @version Id: GPHandlerAdapter.java, v 0.1 2020/4/10 13:44 ex-yipeng Exp $
 */
public class GPHandlerAdapter {

    public boolean supports(Object handler) {
        return handler instanceof GPHandlerMapping;
    }

    public GPModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        GPHandlerMapping handlerMapping = (GPHandlerMapping) handler;
        //每个方法都有形参列表 这里保存的是形参列表
        Map<String, Integer> paramMapping = Maps.newHashMap();
        Annotation[][] pa = handlerMapping.getMethod().getParameterAnnotations();
        for (int i = 0; i < pa.length; i++) {
            for (Annotation a : pa[i]) {
                if (a instanceof GPRequestParam) {
                    String paramName = ((GPRequestParam) a).value();
                    if (!"".equals(paramName.trim())) {
                        paramMapping.put(paramName, i);
                    }

                }
            }
        }
        //动态绑定参数 只处理request和response
        Class<?>[] paramTypes = handlerMapping.getMethod().getParameterTypes();
        for (int i = 0; i < paramTypes.length; i++) {
            Class<?> type = paramTypes[i];
            if (type == HttpServletRequest.class || type == HttpServletResponse.class) {
                paramMapping.put(type.getName(), i);
            }
        }

        Map<String, String[]> reqParamMap = request.getParameterMap();

        Object[] paramValues = new Object[paramTypes.length];

        for (Map.Entry<String, String[]> param : reqParamMap.entrySet()) {
            String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "").replaceAll("\\s", "");
            if (!paramMapping.containsKey(param.getKey())) {
                continue;
            }
            int index = paramMapping.get(param.getKey());
            //因为传过来的都是String 这里转成对应的参数类型
            paramValues[index] = caseStringValue(value, paramTypes[index]);
        }
        if (paramMapping.containsKey(HttpServletRequest.class.getName())) {
            int index = paramMapping.get(HttpServletRequest.class.getName());
            paramValues[index] = request;
        }
        if (paramMapping.containsKey(HttpServletResponse.class.getName())) {
            int index = paramMapping.get(HttpServletResponse.class.getName());
            paramValues[index] = response;
        }
        Object result = handlerMapping.getMethod().invoke(handlerMapping.getController(), paramValues);
        boolean isModelAndView = handlerMapping.getMethod().getReturnType() == GPModelAndView.class;
        if (isModelAndView) {
            return (GPModelAndView) result;
        } else {
            return null;
        }
    }

    private Object caseStringValue(String value, Class<?> clazz) {
        if (clazz == String.class) {
            return value;
        } else if (clazz == Integer.class) {
            return Integer.valueOf(value);
        } else if (clazz == int.class) {
            return Integer.valueOf(value).intValue();
        } else {
            return null;
        }
    }
}
