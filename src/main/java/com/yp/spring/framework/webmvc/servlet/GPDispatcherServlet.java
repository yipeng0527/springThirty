package com.yp.spring.framework.webmvc.servlet;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yp.spring.framework.annotation.GPController;
import com.yp.spring.framework.annotation.GPRequestMapping;
import com.yp.spring.framework.context.GPApplicationContext;
import com.yp.spring.framework.webmvc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ex-yipeng
 * @version Id: GPDispatcherServlet.java, v 0.1 2020/3/30 13:55 ex-yipeng Exp $
 */
public class GPDispatcherServlet extends HttpServlet {

    private final String LOCATION = "contextConfigLocation";

    private final static Logger log = LoggerFactory.getLogger(GPDispatcherServlet.class);

    private List<GPHandlerMapping> handlerMappings = Lists.newArrayList();

    private Map<GPHandlerMapping, GPHandlerAdapter> handlerAdapters = Maps.newHashMap();

    List<GPViewResolver> viewResolvers = Lists.newArrayList();

    private GPApplicationContext context;

    @Override
    public void init(ServletConfig config) throws ServletException {
        //初始化IOC容器
        context = new GPApplicationContext(config.getInitParameter(LOCATION));
        initStrategies(context);
    }

    protected void initStrategies(GPApplicationContext context) {

        //文件上传解析，如果请求类型为multipart将通过MultipartResolver进行文件上传解析
        initMultipartResolver(context);
        //初始化本地语言环境
        initLocaleResolver(context);
        //初始化模板处理器
        initThemeResolver(context);
        //handlerMapping  GPHandlerMapping 用来保存Controller
        initHandlerMappings(context);
        //初始化参数适配器
        initHandlerAdapters(context);
        //初始化异常拦截器
        initHandlerExceptionResolvers(context);
        //初始化视图预处理器
        initRequestToViewNameTranslator(context);
        //初始化视图转换器
        initViewResolvers(context);
        //flash映射管理器
        initFlashMapManager(context);

    }

    private void initMultipartResolver(GPApplicationContext context) {
    }

    private void initLocaleResolver(GPApplicationContext context) {
    }

    private void initThemeResolver(GPApplicationContext context) {
    }

    private void initHandlerExceptionResolvers(GPApplicationContext context) {
    }

    private void initRequestToViewNameTranslator(GPApplicationContext context) {
    }

    private void initFlashMapManager(GPApplicationContext context) {
    }

    /**
     * 将controller中配置的RequestMapping和Method进行一一对应
     *
     * @param context
     */
    private void initHandlerMappings(GPApplicationContext context) {
        String[] beanNames = context.getBeanDefinitionNames();
        try {
            for (String beanName : beanNames) {
                Object controller = context.getBean(beanName);
                Class<?> clazz = controller.getClass();
                if (!clazz.isAnnotationPresent(GPController.class)) {
                    continue;
                }
                String baseUrl = "";
                if (clazz.isAnnotationPresent(GPRequestMapping.class)) {
                    GPRequestMapping requestMapping = clazz.getAnnotation(GPRequestMapping.class);
                    baseUrl = requestMapping.value();
                }
                //扫描所有的public方法
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (!method.isAnnotationPresent(GPRequestMapping.class)) {
                        continue;
                    }
                    GPRequestMapping requestMapping = method.getAnnotation(GPRequestMapping.class);
                    String regex = ("/" + baseUrl + requestMapping.value().replaceAll("\\*", ".*")).replaceAll("/+", "/");
                    Pattern pattern = Pattern.compile(regex);
                    this.handlerMappings.add(new GPHandlerMapping(controller, method, pattern));
                    log.info("Mapping:" + regex + "," + method);
                }
            }
        } catch (Exception e) {
            log.error("initHandlerMappings exception", e);
        }
    }

    private void initHandlerAdapters(GPApplicationContext context) {
        //在初始化阶段我们能做的就是将这些参数的名字或者类型按一定的顺序保存下来
        //因为后面用反射调用的时候 传的形参是一个数组
        //可以通过记录这些参数的位置index 挨个从数组中填值 这样的话就和参数的顺序无关了
        for (GPHandlerMapping handlerMapping : this.handlerMappings) {
            //每个方法有个参数列表 那么这里保存的是形参列表
            this.handlerAdapters.put(handlerMapping, new GPHandlerAdapter());
        }
    }

    private void initViewResolvers(GPApplicationContext context) {
        //拿到模板的存放目录
        String templateRoot = context.getConfig().getProperty("templateRoot");
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();

        File templateRootDir = new File(templateRootPath);
        String[] templates = templateRootDir.list();
        for (int i = 0; i < templates.length; i ++) {
            //这里主要是为了兼容多模板，所有模仿Spring用List保存
            //在我写的代码中简化了，其实只有需要一个模板就可以搞定
            //只是为了仿真，所有还是搞了个List
            this.viewResolvers.add(new GPViewResolver(templateRoot));
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            doDispatch(request, response);
        } catch (Exception e) {
            response.getWriter().write("<font size='25' color='blue'>500 Exception</font><br/>Details:<br/>"
                    + Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]", "")
                    .replaceAll("\\s", "\r\n") + "<font color='green'><i>Copyright@GupaoEdu</i></font>");
            log.error("doDispatch exceptoin", e);
        }
    }

    private void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //根据用户请求得到一个handler
        GPHandlerMapping handler = getHandler(request);
        if (null == handler) {
            processDispatchResult(request, response, new GPModelAndView("404"));
            return;
        }
        GPHandlerAdapter ha = getHandlerAdapter(handler);
        //调用方法得到一个返回值
        GPModelAndView mv = ha.handle(request, response, handler);
        //这一步才是真正的输出
        processDispatchResult(request, response, mv);
    }

    private GPHandlerAdapter getHandlerAdapter(GPHandlerMapping handler) {
        if (this.handlerAdapters.isEmpty()) {
            return null;
        }
        GPHandlerAdapter ha = this.handlerAdapters.get(handler);
        if (ha.supports(handler)) {
            return ha;
        }
        return null;
    }

    private void processDispatchResult(HttpServletRequest request, HttpServletResponse response, GPModelAndView mv) throws Exception {
        if (null == mv) {
            return;
        }
        if (this.viewResolvers.isEmpty()) {
            return;
        }
        for (GPViewResolver vr : this.viewResolvers) {
            GPView view = vr.resolveViewName(mv.getViewName(), null);
            if (null != view) {
                view.render(mv.getModel(), request, response);
                return;
            }
        }
    }

    private GPHandlerMapping getHandler(HttpServletRequest request) {
        if (this.handlerMappings.isEmpty()) {
            return null;
        }
        String url = request.getRequestURI();
        String contextPath = request.getContextPath();
        url = url.replaceAll(contextPath, "").replaceAll("/+", "/");
        for (GPHandlerMapping handler : this.handlerMappings) {
            Matcher matcher = handler.getPattern().matcher(url);
            if (!matcher.matches()) {
                continue;
            }
            return handler;
        }
        return null;
    }
}
