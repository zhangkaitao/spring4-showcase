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
import org.springframework.util.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethodSelector;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-3
 * <p>Version: 1.0
 */
public class DynamicDeployBeans2 {

    protected static final Log logger = LogFactory.getLog(DynamicDeployBeans2.class);

    //RequestMappingHandlerMapping
    private static Method detectHandlerMethodsMethod =
            ReflectionUtils.findMethod(RequestMappingHandlerMapping.class, "detectHandlerMethods", Object.class);
    private static Method getMappingForMethodMethod =
            ReflectionUtils.findMethod(RequestMappingHandlerMapping.class, "getMappingForMethod", Method.class, Class.class);
    private static Method getMappingPathPatternsMethod =
            ReflectionUtils.findMethod(RequestMappingHandlerMapping.class, "getMappingPathPatterns", RequestMappingInfo.class);
    private static Method getPathMatcherMethod =
            ReflectionUtils.findMethod(RequestMappingHandlerMapping.class, "getPathMatcher");
    private static Field handlerMethodsField =
            ReflectionUtils.findField(RequestMappingHandlerMapping.class, "handlerMethods", Map.class);
    private static Field urlMapField =
            ReflectionUtils.findField(RequestMappingHandlerMapping.class, "urlMap", MultiValueMap.class);


    private static final String SCRIPT_FACTORY_POST_PROCESSOR_BEAN_NAME =
            "org.springframework.scripting.config.scriptFactoryPostProcessor";

    private Long refreshCheckDelay = -1L;

    static {
        detectHandlerMethodsMethod.setAccessible(true);
        getMappingForMethodMethod.setAccessible(true);
        getMappingPathPatternsMethod.setAccessible(true);
        getPathMatcherMethod.setAccessible(true);
        handlerMethodsField.setAccessible(true);
        urlMapField.setAccessible(true);
    }


    private ApplicationContext ctx;
    private DefaultListableBeanFactory beanFactory;

    private boolean hasRegisterScriptFactoryPostProcessor = false;


    @Autowired
    public void setApplicationContext(ApplicationContext ctx) {
        if (!DefaultListableBeanFactory.class.isAssignableFrom(ctx.getAutowireCapableBeanFactory().getClass())) {
            throw new IllegalArgumentException("BeanFactory must be DefaultListableBeanFactory type");
        }
        this.ctx = ctx;
        this.beanFactory = (DefaultListableBeanFactory) ctx.getAutowireCapableBeanFactory();
    }

    public void setRefreshCheckDelay(Long refreshCheckDelay) {
        this.refreshCheckDelay = refreshCheckDelay;
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


    public void registerGroovyController(String scriptLocation) {
        registerScriptFactoryPostProcessorIfNecessary();
        // Create script factory bean definition.
        GenericBeanDefinition bd = new GenericBeanDefinition();
        bd.setBeanClassName(GroovyScriptFactory.class.getName());

        bd.setAttribute(ScriptFactoryPostProcessor.LANGUAGE_ATTRIBUTE, "groovy");
        bd.setAttribute(ScriptFactoryPostProcessor.REFRESH_CHECK_DELAY_ATTRIBUTE, refreshCheckDelay);

        bd.setAttribute(ScriptFactoryPostProcessor.PROXY_TARGET_CLASS_ATTRIBUTE, true);

        ConstructorArgumentValues cav = bd.getConstructorArgumentValues();
        int constructorArgNum = 0;
        cav.addIndexedArgumentValue(constructorArgNum++, scriptLocation);

        String controllerBeanName = scriptLocation;
        beanFactory.registerBeanDefinition(controllerBeanName, bd);
        addHandler(controllerBeanName);
    }


    private void addHandler(String controllerBeanName) {
        RequestMappingHandlerMapping requestMappingHandlerMapping = requestMappingHandlerMapping();

        //remove old
        Class<?> handlerType = ctx.getType(controllerBeanName);
        final Class<?> userType = ClassUtils.getUserClass(handlerType);

        Map handlerMethods = (Map) ReflectionUtils.getField(handlerMethodsField, requestMappingHandlerMapping);
        MultiValueMap urlMapping = (MultiValueMap) ReflectionUtils.getField(urlMapField, requestMappingHandlerMapping);

        final RequestMappingHandlerMapping innerRequestMappingHandlerMapping = requestMappingHandlerMapping;
        Set<Method> methods = HandlerMethodSelector.selectMethods(userType, new ReflectionUtils.MethodFilter() {
            @Override
            public boolean matches(Method method) {
                return ReflectionUtils.invokeMethod(
                        getMappingForMethodMethod,
                        innerRequestMappingHandlerMapping,
                        method, userType) != null;
            }
        });

        for (Method method : methods) {
            RequestMappingInfo mapping =
                    (RequestMappingInfo) ReflectionUtils.invokeMethod(getMappingForMethodMethod, requestMappingHandlerMapping, method, userType);
            handlerMethods.remove(mapping);

            Set<String> patterns =
                    (Set<String>) ReflectionUtils.invokeMethod(getMappingPathPatternsMethod, requestMappingHandlerMapping, mapping);

            PathMatcher pathMatcher =
                    (PathMatcher) ReflectionUtils.invokeMethod(getPathMatcherMethod, requestMappingHandlerMapping);

            for (String pattern : patterns) {
                if (!pathMatcher.isPattern(pattern)) {
                    urlMapping.remove(pattern);
                }
            }
        }

        //spring 3.1 开始
        ReflectionUtils.invokeMethod(detectHandlerMethodsMethod, requestMappingHandlerMapping, controllerBeanName);


    }

    private RequestMappingHandlerMapping requestMappingHandlerMapping() {
        try {
            return ctx.getBean(RequestMappingHandlerMapping.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("applicationContext must has RequestMappingHandlerMapping");
        }
    }


    private void registerScriptFactoryPostProcessorIfNecessary() {
        if (!hasRegisterScriptFactoryPostProcessor) {
            hasRegisterScriptFactoryPostProcessor = beanFactory.containsBeanDefinition(SCRIPT_FACTORY_POST_PROCESSOR_BEAN_NAME);
            if (!hasRegisterScriptFactoryPostProcessor) {
                BeanDefinition beanDefinition = new RootBeanDefinition(ScriptFactoryPostProcessor.class);
                beanFactory.registerBeanDefinition(SCRIPT_FACTORY_POST_PROCESSOR_BEAN_NAME, beanDefinition);
                beanFactory.addBeanPostProcessor(beanFactory.getBean(ScriptFactoryPostProcessor.class));

            }
        }
    }

}
