package net.bncloud.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

public class ApplicationContextProvider implements ApplicationContextAware {
    private static ApplicationContext context;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    /**
     * 通过name获取 Bean
     */
    public static Object getBean(String name) {
        if(getApplicationContext()!=null) {
            return getApplicationContext().getBean(name);
        }
        return null;
    }

    /**
     * 通过class获取Bean
     */
    public static <T> T getBean(Class<T> clazz) {
        if(getApplicationContext()!=null) {
            try {
                return getApplicationContext().getBean(clazz);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        if(getApplicationContext()!=null) {
            return getApplicationContext().getBean(name, clazz);
        }
        return null;
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> type) {
        return getApplicationContext().getBeansOfType(type);
    }


}
