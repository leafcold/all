package first.core.context;/*
 *创建者: zsq
 *创建时间:2020/3/21 21:23
 */

import first.core.net.http.ParseUrlUtil;
import io.netty.channel.MultithreadEventLoopGroup;
import javafx.util.Pair;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static first.core.log.Logger.MLOG;

/**
 * 构建消息
 */
public class Context {

    static public Map<Short, Pair<Object, Method>> ContextMap = new HashMap<Short, Pair<Object, Method>>();

    static public Map<String, Pair<Object, Method>> HttpContextMap = new HashMap<String, Pair<Object, Method>>();

    static public Map<Short, Method> protoMap = new HashMap<Short, Method>();

    static public Map<String,Method> HttpMap =new HashMap<String, Method>();


    static public void initBeans(ConfigurableApplicationContext context, Class nowClass) throws InvocationTargetException, IllegalAccessException, NoSuchFieldException, NoSuchMethodException {
        //去获取包底下的bean 进行封装
        Map<String, BeanDefinition> mapBean = new HashMap<String, BeanDefinition>();
        String[] strings = context.getBeanDefinitionNames();
        for (String beanName : strings) {
            mapBean.put(beanName, context.getBeanFactory().getBeanDefinition(beanName));
        }
        ComponentScan anno = (ComponentScan) nowClass.getAnnotation(ComponentScan.class);
        String[] basePackages = anno.value();
        //first is 类名 second 包名
        List<Pair<String, String>> stringList = new ArrayList<Pair<String, String>>();
        for (BeanDefinition beanDefinition : mapBean.values()) {
            String bean = beanDefinition.getBeanClassName();
            if (bean == null) {
                continue;
            }
            for (String basePackage : basePackages) {
                if (bean.contains(basePackage)) {
                    Pair<String, String> pair;
                    pair = new Pair<String, String>(bean, bean.split("\\.")[bean.split("\\.").length - 1]);
                    stringList.add(pair);
                }
            }
        }
        //解析包底下的类 存储到缓存里 map<int,pair<Object,method>>
        for (Pair<String, String> className : stringList) {
            //去获取
            try {
                Object contextBean = context.getBean(toLowerCaseFirstOne(className.getValue()));
                Method[] classMethods = Class.forName(className.getKey()).getDeclaredMethods();
                for (Method classMethod : classMethods) {
                    if(classMethod.getAnnotation(FunctionDoName.class)!=null)
                    {
                        short value = classMethod.getAnnotation(FunctionDoName.class).value();
                        Pair<Object, Method> pair = new Pair<Object, Method>(contextBean, classMethod);
                        protoMap.put(value, classMethod.getParameterTypes()[0].getMethod("parseFrom",byte[].class));
                        ContextMap.put(value, pair);
                    }
                    else if (classMethod.getAnnotation(HttpContext.class)!=null){
                        String value = classMethod.getAnnotation(HttpContext.class).value();
                        Pair<Object, Method> pair = new Pair<Object, Method>(contextBean, classMethod);
                        HttpMap.put(value, classMethod.getParameterTypes()[0].getMethod("parseFrom", ParseUrlUtil.class));
                        HttpContextMap.put(value, pair);
                    }
                }
                if (ContextMap.size() == protoMap.size()) {
                    MLOG.info("完成协议加载");
                }else {
                    MLOG.info("协议加载失败");
                }

            } catch (Exception e) {
               e.printStackTrace();
            }

        }

    }

    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) return s;
        else return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }
}
