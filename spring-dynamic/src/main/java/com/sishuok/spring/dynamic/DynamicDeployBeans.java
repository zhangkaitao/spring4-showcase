package com.sishuok.spring.dynamic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.scripting.groovy.GroovyScriptFactory;
import org.springframework.scripting.support.ScriptFactoryPostProcessor;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-3
 * <p>Version: 1.0
 */
public class DynamicDeployBeans {

    protected static final Log logger = LogFactory.getLog(DynamicDeployBeans.class);

    private ApplicationContext ctx;
    private DefaultListableBeanFactory beanFactory;

    @Autowired
    public void setApplicationContext(ApplicationContext ctx) {
        if (!DefaultListableBeanFactory.class.isAssignableFrom(ctx.getAutowireCapableBeanFactory().getClass())) {
            throw new IllegalArgumentException("BeanFactory must be DefaultListableBeanFactory type");
        }
        this.ctx = ctx;
        this.beanFactory = (DefaultListableBeanFactory) ctx.getAutowireCapableBeanFactory();
    }

    public void registerBean(Class<?> beanClass) {
        registerBean(null, beanClass);
    }

    public void registerBean(String beanName, Class<?> beanClass) {
        Assert.notNull(beanClass, "register bean class must not null");
        GenericBeanDefinition bd = new GenericBeanDefinition();
        bd.setBeanClass(beanClass);

        if (StringUtils.hasText(beanName)) {
            beanFactory.registerBeanDefinition(beanName, bd);
        } else {
            BeanDefinitionReaderUtils.registerWithGeneratedName(bd, beanFactory);
        }

    }

    public void registerController(Class<?> controllerClass) {
        Assert.notNull(controllerClass, "register controller bean class must not null");
        if (!WebApplicationContext.class.isAssignableFrom(ctx.getClass())) {
            throw new IllegalArgumentException("applicationContext must be WebApplicationContext type");
        }

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(controllerClass);

        String controllerBeanName =
                BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, beanFactory);


        addHandler(controllerBeanName);

    }

    private static Method determineUrlsForHandlerMethod =
            ReflectionUtils.findMethod(DefaultAnnotationHandlerMapping.class, "determineUrlsForHandler", String.class);

    private static Method registerHandlerMethod =
            ReflectionUtils.findMethod(DefaultAnnotationHandlerMapping.class, "registerHandler", String[].class, String.class);

    private static Method detectHandlerMethodsMethod =
            ReflectionUtils.findMethod(RequestMappingHandlerMapping.class, "detectHandlerMethods", Object.class);

    static {
        detectHandlerMethodsMethod.setAccessible(true);
        registerHandlerMethod.setAccessible(true);
        detectHandlerMethodsMethod.setAccessible(true);
    }

    private void addHandler(String controllerBeanName) {
        DefaultAnnotationHandlerMapping annotationHandlerMapping = null;
        RequestMappingHandlerMapping requestMappingHandlerMapping = null;


        if ((annotationHandlerMapping = defaultAnnotationHandlerMapping()) != null) {
            //spring 3.1 之前
            String[] urls = (String[]) ReflectionUtils.invokeMethod(determineUrlsForHandlerMethod, annotationHandlerMapping, controllerBeanName);
            if (!ObjectUtils.isEmpty(urls)) {
                // URL paths found: Let's consider it a handler.
                ReflectionUtils.invokeMethod(registerHandlerMethod, annotationHandlerMapping, urls, controllerBeanName);
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Rejected bean name '" + controllerBeanName + "': no URL paths identified");
                }
            }

        } else if ((requestMappingHandlerMapping = requestMappingHandlerMapping()) != null) {
            //spring 3.1 开始
            ReflectionUtils.invokeMethod(detectHandlerMethodsMethod, requestMappingHandlerMapping, controllerBeanName);
        } else {
            throw new IllegalArgumentException("applicationContext must contains DefaultAnnotationHandlerMapping or RequestMappingHandlerMapping bean");
        }


    }

    private DefaultAnnotationHandlerMapping defaultAnnotationHandlerMapping() {
        try {
            return ctx.getBean(DefaultAnnotationHandlerMapping.class);
        } catch (Exception e) {
            return null;
        }
    }

    private RequestMappingHandlerMapping requestMappingHandlerMapping() {
        try {
            return ctx.getBean(RequestMappingHandlerMapping.class);
        } catch (Exception e) {
            return null;
        }
    }

    public void registerGroovyController(String scriptLocation) {
        registerScriptFactoryPostProcessorIfNecessary();
        // Create script factory bean definition.
        GenericBeanDefinition bd = new GenericBeanDefinition();
        bd.setBeanClassName(GroovyScriptFactory.class.getName());

        bd.setAttribute(ScriptFactoryPostProcessor.LANGUAGE_ATTRIBUTE, "groovy");
        Long refreshCheckDelay = -1L;
        bd.setAttribute(ScriptFactoryPostProcessor.REFRESH_CHECK_DELAY_ATTRIBUTE, refreshCheckDelay);

        bd.setAttribute(ScriptFactoryPostProcessor.PROXY_TARGET_CLASS_ATTRIBUTE, true);

        ConstructorArgumentValues cav = bd.getConstructorArgumentValues();
        int constructorArgNum = 0;
        cav.addIndexedArgumentValue(constructorArgNum++, scriptLocation);

        String controllerBeanName =
                BeanDefinitionReaderUtils.registerWithGeneratedName(bd, beanFactory);

        System.out.println(ctx.getBean(controllerBeanName));

        addHandler(controllerBeanName);
    }

    private static final String SCRIPT_FACTORY_POST_PROCESSOR_BEAN_NAME =
            "org.springframework.scripting.config.scriptFactoryPostProcessor";

    private void registerScriptFactoryPostProcessorIfNecessary() {
        BeanDefinition beanDefinition = null;
        if (!beanFactory.containsBeanDefinition(SCRIPT_FACTORY_POST_PROCESSOR_BEAN_NAME)) {
            beanDefinition = new RootBeanDefinition(ScriptFactoryPostProcessor.class);
            beanFactory.registerBeanDefinition(SCRIPT_FACTORY_POST_PROCESSOR_BEAN_NAME, beanDefinition);
            beanFactory.addBeanPostProcessor(beanFactory.getBean(ScriptFactoryPostProcessor.class));
        }
    }

}
